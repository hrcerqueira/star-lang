package io.hnrc.star.lang

import io.hnrc.star.lang.utils.MidPointIteratorFactory
import org.scalatest.funsuite.AnyFunSuite

class MidPointGeneratorTest extends AnyFunSuite:

  test("first element is zero"):
    val point = MidPointIteratorFactory.iterator.take(1).toList.head
    assert(point == (0, 0))


