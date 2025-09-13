package io.hnrc.star.lang.runner

import io.hnrc.star.lang.parser.StarNodeList

sealed trait ExecutionContinuation

object Continue extends ExecutionContinuation
object ReturnFromStar extends ExecutionContinuation
case class PokeStar(star: String) extends ExecutionContinuation
case class RunRepeatingControl(nodes: StarNodeList) extends ExecutionContinuation