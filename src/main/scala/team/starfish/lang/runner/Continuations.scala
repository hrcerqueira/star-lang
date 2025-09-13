package team.starfish.lang.runner

import team.starfish.lang.parser.StarNodeList

sealed trait ExecutionContinuation

object Continue extends ExecutionContinuation
object ReturnFromStar extends ExecutionContinuation
case class PokeStar(star: String) extends ExecutionContinuation
case class RunRepeatingControl(nodes: StarNodeList) extends ExecutionContinuation