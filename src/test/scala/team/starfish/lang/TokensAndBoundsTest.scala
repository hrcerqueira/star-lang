package team.starfish.lang

import org.scalatest.funsuite.AnyFunSuite
import team.starfish.lang.tokenizer.StarMetaInstructions.PADDING
import team.starfish.lang.tokenizer.{StarCoordinates, StarTokens}
import team.starfish.lang.utils.withBounds

class TokensAndBoundsTest extends AnyFunSuite:

  test("set bounds simple"):
    val tokens = StarTokens(
      identifier = "A",
      north     = 1.mkLeg,
      east      = 1.mkLeg,
      southEast = 1.mkLeg,
      southWest = 1.mkLeg,
      west      = 1.mkLeg,
      coordinates = StarCoordinates(10, 10),
    )

    val withBounds = tokens.withBounds

    assert(withBounds.legSize == 2)
    assert(withBounds.maxX == 13)
    assert(withBounds.minX == 7)
    assert(withBounds.minY == 8)
    assert(withBounds.maxY == 13)

  test("set bounds different leg sizes"):
    val tokens = StarTokens(
      identifier = "A",
      north     = 2.mkLeg,
      east      = 4.mkLeg,
      southEast = 9.mkLeg,
      southWest = 5.mkLeg,
      west      = 2.mkLeg,
      coordinates = StarCoordinates(100, 100),
    )

    val withBounds = tokens.withBounds

    assert(withBounds.legSize == 18)
    assert(withBounds.maxX == 119)
    assert(withBounds.minX == 81)
    assert(withBounds.minY == 90)
    assert(withBounds.maxY == 111)

  extension (size: Int)
    def mkLeg = List.fill(size)(PADDING)

    def mkStar = StarTokens(
      identifier = "A",
      north     = (size / 2).mkLeg,
      east      = size.mkLeg,
      southEast = (size / 2).mkLeg,
      southWest = (size / 2).mkLeg,
      west      = size.mkLeg,
      coordinates = StarCoordinates(100, 100),
    )

  extension (tokens: StarTokens)
    def apply(x: Int, y: Int) = tokens.copy(
      coordinates = StarCoordinates(x, y)
    )
