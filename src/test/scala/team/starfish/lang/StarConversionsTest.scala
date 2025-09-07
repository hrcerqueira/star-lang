package team.starfish.lang

import org.scalatest.funsuite.AnyFunSuite
import team.starfish.lang.alt.AltTokenizer
import team.starfish.lang.andThen

import scala.io.Source

class StarConversionsTest extends AnyFunSuite:

  test("convert hello world"):
    convertAndAssert("helloworld")

  private def convertAndAssert(seaFile: String) =
    val input = Source.fromResource(s"samples/$seaFile.sea").getLines().mkString("\n")
    val tokens = AltTokenizer.tokenize(input)
    val converted = StarWriter(BlandDialect, Map()).write(tokens)

    val starTokenizer = MainSyntaxStarTokenizer(BlandDialect)
    val tokensFromStars = starTokenizer.tokenize(converted)

    val outputFromOriginal = StarParser.parse(tokens).andThen(StarRunner.run(_, BlandDialect.mainStar))
    val outputFromStars = StarParser.parse(tokens).andThen(StarRunner.run(_, BlandDialect.mainStar))
    assert(outputFromOriginal == outputFromStars)
