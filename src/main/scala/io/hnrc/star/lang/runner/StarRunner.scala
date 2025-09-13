package io.hnrc.star.lang.runner

import io.hnrc.star.lang.parser.{Debug, Decrement, Done, IfReferenceNotZero, IfSelfFullSpin, IfSelfNotZero, Increment, JumpToStar, Print, Read, Sea, StarControlNode, StarFish, StarNode, StarNodeList}
import io.hnrc.star.lang.*
import io.hnrc.star.lang.utils.{also, utf8Chars}

import scala.collection.mutable

case class StackEntry(
  star: StarFish,
  executed: StarNodeList,
  toExecute: StarNodeList,
  isControl: Boolean,
  startState: Map[String, Int],
  startInput: List[String],
)

private class StarRunner(sea: Sea, userInput: String = "", logLevel: String = "none"):

  private type State = mutable.Map[String, Int]

  private[runner] val executionStack = mutable.Stack[StackEntry]()
  private[runner] val state: State = mutable.Map[String, Int]()
  private val output: StringBuilder = new StringBuilder()

  private val logger = StarLogger(logLevel, this)
  import logger._

  private var unconsumedInput = userInput.utf8Chars

  def run(star: StarFish): String =
    star.addToStack()
    runStack()
    output.toString()

  private def runStack() =
    while executionStack.nonEmpty do
      val entry = executionStack.head
      entry.star.executeNodes(entry.toExecute)

  private def pushToStack(star: StarFish, nodes: StarNodeList, isControl: Boolean): Unit =
    trace:
      s"Pushing to stack: ${star.symbol} -> ${printNodes(nodes)}"

    executionStack.push(
      StackEntry(
        star,
        Nil,
        nodes,
        isControl,
        state.toMap,
        unconsumedInput
      )
    )

  private def popFromStack() =
    executionStack.pop().also: entry =>
      trace("Popped from stack")

  private def popWhileStarIs(star: StarFish): Unit =
    if executionStack.nonEmpty && executionStack.head.star.symbol == star.symbol then
      popFromStack()
      popWhileStarIs(star)

  private def markNodeExecuted(): Unit =
    val entry = executionStack.pop()
    val executedNode = entry.toExecute.head
    executionStack.push(
      entry.copy(
        executed = entry.executed :+ executedNode,
        toExecute = entry.toExecute.tail,
      ))


  extension (star: StarFish)
    private def addToStack(): Unit =
      trace("*** Start execution")
      findLegAndAddToStack()

    private def findLegAndAddToStack(): Unit =
      trace(s">>> Next leg, value is $ownValue")
      val (legName, executionLeg) = ownValue % 5 match
        case 0 =>
          "north" -> star.north
        case 1 =>
          trace(s"Executing east leg")
          "east" -> star.east
        case 2 =>
          "southEast" -> star.southEast
        case 3 =>
          "southWest" -> star.southWest
        case 4 =>
          "west" -> star.west

      val stateBeforeLeg = state.toMap

      pushToStack(star, executionLeg, isControl = false)


    private def executeNodes(nodes: StarNodeList): Unit = nodes match
      case Nil =>
        val popped = popFromStack()
        if !popped.isControl then
          checkNotInInfiniteLoop(popped)
          findLegAndAddToStack()
      case node :: rest =>
        executeNode(node).also: continuation =>
          trace(s"Continuation: ${continuation.getClass.getSimpleName}")
        match
          case ReturnFromStar =>
            popWhileStarIs(star)
          case RunRepeatingControl(nodes) =>
            pushToStack(star, nodes, isControl = true)
          case PokeStar(nextStar) =>
            if nextStar == star.symbol && rest.headOption.contains(Done) then
              // tail recursion
              popWhileStarIs(star)
              findLegAndAddToStack()
            else
              markNodeExecuted()
              sea(nextStar).addToStack()
          case Continue =>
            markNodeExecuted()


    private def checkNotInInfiniteLoop(popped: StackEntry): Unit =
      if popped.startState == state.toMap && popped.startInput == unconsumedInput then
        printStackTrace()
        throw RuntimeException(
          s"""
             |Leg execution done with no state change or return, this is an infinite loop.
             |Executing leg ${popped.star.ownValue % 5} of star ${star.symbol}.
             |State: ${state.toList.map(e => s"${e._1} -> ${e._2}").mkString("\n")}""".stripMargin)

    private def executeNode(node: StarNode): ExecutionContinuation = node match
      case Done =>
        trace(s"Star execution done signal")
        ReturnFromStar

      case Increment =>
        trace(s"Incrementing value: $ownValue -> ${ownValue + 1}")
        state.update(star.symbol, ownValue + 1)
        Continue

      case Decrement =>
        trace(s"Decrementing value: $ownValue -> ${ownValue - 1}")
        if ownValue == 0 then
          throw new RuntimeException(s"Cannot decrement value below 0")
        state.update(star.symbol, ownValue - 1)
        Continue

      case Read =>
        trace(s"Reading from stdin")
        unconsumedInput match
          case Nil =>
            trace(s"No more input, setting value to 0")
            state.update(star.symbol, 0)
          case head :: tail =>
            val read = head.codePoints().toArray.head
            trace(s"Read $read")
            state.update(star.symbol, head.codePoints().toArray.head)
            unconsumedInput = tail

        Continue

      case Print =>
        trace(s"Printing value")
        if ownValue >= 32 then
          val toPrint = Character.toChars(ownValue)
          output.append(new String(toPrint))

        Continue

      case Debug =>
        printStackTrace()
        Continue


      case control: IfSelfNotZero =>
        executeControl(control, ownValue == 0, s"Checking if $ownValue is not zero")

      case control@IfReferenceNotZero(starName, _) =>
        val targetStarValue = sea(starName).ownValue
        executeControl(control, targetStarValue == 0, s"Checking if $targetStarValue of $starName is not zero")

      case control: IfSelfFullSpin =>
        executeControl(control, ownValue >= 0 && ownValue < 5, s"Checking if $ownValue is in range ]0, 5] (full spin)")

      case JumpToStar(jmpStar) =>
        trace(s"Jumping to $jmpStar")
        PokeStar(jmpStar)

    private def executeControl(node: StarControlNode, check: => Boolean, checkName: String): ExecutionContinuation =
      trace(checkName)
      if !check then
        trace(s"condition met, executing control nodes")
        RunRepeatingControl(node.nodes)
      else
        trace(s"Condition not met, skipping control nodes")
        Continue


    private def ownValue: Int =
      state.getOrElse(star.symbol, 0)


extension (sea: Sea)
  private def apply(starName: String) = sea.starfishes.find(_.symbol == starName)
    .getOrElse(throw new RuntimeException(s"Star not found, $starName"))

object StarRunner:
  def run(sea: Sea, mainStar: String, userInput: String = "", logLevel: String = "none"): String =
    val runner = new StarRunner(sea, userInput, logLevel)
    runner.run(sea(mainStar))


