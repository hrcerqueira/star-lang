package team.starfish.lang

import team.starfish.lang.ExecutionContinuation.{CONTINUE, RETURN_FROM_STAR}

import scala.collection.mutable

private class StarRunner(sea: Sea, userInput: String = "", logLevel: String = "none"):

  private type State = mutable.Map[String, Int]

  private val state: State = mutable.Map[String, Int]()
  private val output: mutable.ListBuffer[Char] = mutable.ListBuffer[Char]()

  private var unconsumedInput = userInput.utf8Chars

  def run(star: StarFish): String =
    star.execute()
    output.mkString

  private var depth = 0

  extension (star: StarFish)
    private def execute(incDepth: Boolean = true): Unit =
      if incDepth then
        depth += 1
        trace("*** Start execution")
      while true do
        findAndExecuteLeg() match
          case RETURN_FROM_STAR =>
            if incDepth then
              trace(s"Star execution done")
              depth -= 1
            return
          case CONTINUE =>
            trace(s"<<< Leg execution done")


    private def findAndExecuteLeg(): ExecutionContinuation =
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
      val unconsumedInputBeforeLeg = unconsumedInput.size

      def statesTransition(prefix: String, cmp: Map[String, Int]) =
        val states = (stateBeforeLeg.keys.toSet ++ cmp.keys.toSet).toList.sorted
        states
          .map: s =>
            s"$logSpacing$prefix $s: ${stateBeforeLeg.getOrElse(s, 0)} -> ${cmp.getOrElse(s, 0)}"
          .mkString("\n")


      debug:
        s"Executing ${star.symbol}.$legName leg:\n${statesTransition(">", stateBeforeLeg)})"

      val continuation = executeNodes(executionLeg)

      val stateAfterLeg = state.toMap
      val unconsumedInputAfterLeg = unconsumedInput.size

      debug:
        s"Executed ${star.symbol}.$legName leg:\n${statesTransition("<", stateAfterLeg)})"

      if continuation == CONTINUE && stateBeforeLeg == stateAfterLeg && unconsumedInputBeforeLeg == unconsumedInputAfterLeg then
        throw RuntimeException(
          s"""
            |Leg execution done with no state change or return, this is an infinite loop.
            |Executing leg ${executionLeg} of star ${star.symbol}.
            |State: ${state.toList.map(e => s"${e._1} -> ${e._2}").mkString("\n")}""".stripMargin)

      continuation


    private def executeNodes(nodes: StarNodeList): ExecutionContinuation = nodes match
      case Nil =>
        trace(s"No more instructions in leg")
        CONTINUE
      case node :: rest =>
        executeNode(node) match
          case RETURN_FROM_STAR => RETURN_FROM_STAR
          case  _ => executeNodes(rest)


    private def executeNode(node: StarNode): ExecutionContinuation = node match
      case Done =>
        trace(s"Star execution done signal")
        RETURN_FROM_STAR

      case Increment =>
        trace(s"Incrementing value: $ownValue -> ${ownValue + 1}")
        state.update(star.symbol, ownValue + 1)
        CONTINUE

      case Decrement =>
        trace(s"Decrementing value: $ownValue -> ${ownValue - 1}")
        if ownValue == 0 then
          throw new RuntimeException(s"Cannot decrement value below 0")
        state.update(star.symbol, ownValue - 1)
        CONTINUE

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

        CONTINUE

      case Print =>
        trace(s"Printing value")
        val toPrint = ownValue.toChar
        if !Character.isISOControl(toPrint) then output.append(toPrint)
        CONTINUE

      case control: IfSelfNotZero =>
        executeControl(control, ownValue == 0, s"Checking if $ownValue is not zero")

      case control@IfReferenceNotZero(starName, _) =>
        val targetStarValue = sea(starName).ownValue
        executeControl(control, targetStarValue == 0, s"Checking if $targetStarValue of $starName is not zero")

      case control: IfSelfDivisionNotZero =>
        executeControl(control, ownValue >= 0 && ownValue < 5, s"Checking if $ownValue is in range ]0, 5]")

      case JumpToStar(jmpStar) =>
        trace(s"Jumping to $jmpStar")
        sea(jmpStar).execute(jmpStar != star.symbol)
        CONTINUE

    private def executeControl(node: StarControlNode, check: => Boolean, checkName: String): ExecutionContinuation =
      trace(checkName)
      if !check then
        trace(s"condition met, executing control nodes")
        depth += 1
        val continuation = executeNodes(node.nodes)
        depth -= 1
        if continuation == CONTINUE then
          executeNode(node)
        else
          continuation
      else
        trace(s"Condition not met, skipping control nodes")
        CONTINUE


    private def ownValue: Int =
      state.getOrElse(star.symbol, 0)

    private def trace(message: => String): Unit =
      if logLevel == "trace" then
        log(message)

    private def debug(message: => String): Unit =
      if logLevel == "debug" then
        log(message)
      else
        trace(message)

    private def log(message: => String): Unit =
      println(s"$logSpacing[${star.symbol}] $message")

    private def logSpacing = " ".repeat(depth)

extension (sea: Sea)
  private def apply(starName: String) = sea.starfishes.find(_.symbol == starName)
    .getOrElse(throw new RuntimeException(s"Star not found, $starName"))

private enum ExecutionContinuation:
  case CONTINUE, RETURN_FROM_STAR

object StarRunner:
  def run(sea: Sea, mainStar: String, userInput: String = "", logLevel: String = "none"): String =
    val runner = new StarRunner(sea, userInput, logLevel)
    runner.run(sea(mainStar))


