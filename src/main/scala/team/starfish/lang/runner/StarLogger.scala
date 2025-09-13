package team.starfish.lang.runner

import team.starfish.lang.parser.{Debug, Decrement, Done, IfReferenceNotZero, IfSelfFullSpin, IfSelfNotZero, Increment, JumpToStar, Print, Read, StarControlNode, StarNodeList}

class StarLogger(logLevel: String, runner: StarRunner):

  private def stack = runner.executionStack
  private def state = runner.state

  private def depth = runner.executionStack.size

  private def logSpacing = " ".repeat(depth)

  def trace(message: => String): Unit =
    if logLevel == "trace" then
      log(message)

  def debug(message: => String): Unit =
    if logLevel == "debug" then
      log(message)
    else
      trace(message)

  private def log(message: => String): Unit =
    stack.headOption.match
      case Some(entry) =>
        println(s"$logSpacing[${entry.star.symbol}] $message")
      case None => println(message)

  def printNodes(nodes: StarNodeList) =
    nodes.map:
      case Increment => "+"
      case Decrement => "-"
      case Print => "."
      case Read => "?"
      case Done => "!"
      case Debug => ""
      case control: IfReferenceNotZero => s"{${control.star}}_]"
      case control: IfSelfNotZero => "[_]"
      case control: IfSelfFullSpin => "{_]"
      case jump: JumpToStar => jump.star
    .mkString

  def printStackTrace(): Unit =

    val traceItems = (stack.toList :+ null).zip(null :: stack.toList).dropRight(1)

    println("=".repeat(100))

    traceItems.zipWithIndex.foreach:
      case ((currentEntry, nextEntry), index) =>
        val entry = currentEntry.nn
        val star = entry.star
        val startState = entry.startState
        val atJumpState = if nextEntry == null then state.toMap else nextEntry.startState

        val prefix = " ".repeat(traceItems.length - index)
        val stateString = statesRepr(" " + prefix, startState, atJumpState)
        val nodes = List(printNodes(entry.executed), printNodes(entry.toExecute))
          .filter(_.nonEmpty)
          .mkString(" (_) ")

        val entryName = if entry.nn.isControl then
          s"]${star.symbol}] $nodes"
        else
          s">${star.symbol}> $nodes"


        println(s"$prefix$entryName\n" + stateString)

    println("=".repeat(100))


  private def statesRepr(prefix: String, start: Map[String, Int], state: Map[String, Int]): String =
    val states = (state.keys.toSet ++ start.keys.toSet).toList.sorted
    states
      .map: s =>
        val before = start.getOrElse(s, 0)
        val after = state.getOrElse(s, 0)
        s"$prefix$s: ${start.getOrElse(s, 0)}" + (if before == after then "" else  s" -> ${state.getOrElse(s, 0)}")
      .mkString("\n")



