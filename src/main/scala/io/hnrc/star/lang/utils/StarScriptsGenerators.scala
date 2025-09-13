package io.hnrc.star.lang.utils

type StarScriptGenerator = (args: List[String]) => String

val ReverseGenerator: StarScriptGenerator = args =>
  val size = if args.isEmpty then 52 else args.head.toInt

  val chars = ('a' to 'z').toList ++ ('A' to 'Z').toList

  if chars.length < size then
    println("Invalid size")
    System.exit(1)

  val sb = StringBuilder()

  val starNames = '$' :: chars.take(size)

  starNames.zipWithIndex.foreach:
    case (name, index) =>
      val next = if index < size then starNames(index + 1).toString else ""
      sb.append(
        s"""
          |$name
          |+
          |?$next.!
          |""".stripMargin)

  sb.toString()

val StarScriptGenerators = Map[String, StarScriptGenerator](
  "reverse" -> ReverseGenerator,
)
