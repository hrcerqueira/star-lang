package team.starfish.lang

import org.scalatest.funsuite.AnyFunSuite
import team.starfish.lang.StarInstruction.*

class ParserTest extends AnyFunSuite:
  
  test("parse single star"):
    val starTokens = StarTokens(
      identifier = "A",
      north     = INCREMENT :: DECREMENT :: BEGIN_CONTROL :: PRINT :: END_CONTROL :: Nil,
      east      = BEGIN_CONTROL_DIVISION :: BEGIN_CONTROL :: BEGIN_CONTROL :: StarIdentifier("B") :: Nil,
      southEast = Nil,
      southWest = Nil,
      west      = Nil,
    )
    val sea = StarParser.parse(List(starTokens))
    assert(sea.starfishes.size == 1)
    assert(sea.starfishes.head.symbol == "A")
    assert(sea.starfishes.head.north.size == 3)
    assert(sea.starfishes.head.north(2).asInstanceOf[IfSelfNotZero].nodes.size == 1)

