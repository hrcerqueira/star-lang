package team.starfish.lang

import org.scalatest.funsuite.AnyFunSuite
import team.starfish.lang.utils.MidPointIteratorFactory

class MidPointGeneratorTest extends AnyFunSuite:

  test("first element is zero"):
    val point = MidPointIteratorFactory.iterator.take(1).toList.head
    assert(point == (0, 0))


