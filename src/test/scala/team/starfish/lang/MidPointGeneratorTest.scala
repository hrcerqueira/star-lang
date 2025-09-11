package team.starfish.lang

import org.scalatest.funsuite.AnyFunSuite

class MidPointGeneratorTest extends AnyFunSuite:

  test("first element is zero"):
    val point = MidPointIteratorFactory.iterator.take(1).toList.head
    assert(point == (0, 0))


  test("radius 1"):

    val grid = Array.fill(100, 100)("⚫")
    grid(50)(50) = "🌟"

    val points = MidPointIteratorFactory.iterator.take(3000)

    points.foreach: point =>
      if grid(point._2 + 50)(point._1 + 50) == "⚫" then
        grid(point._2 + 50)(point._1 + 50) = "\uD83D\uDFE0"

    val out = grid.reverse.map(_.mkString).mkString("\n")
    println(out)