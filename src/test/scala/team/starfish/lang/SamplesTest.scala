package team.starfish.lang

import org.scalatest.funsuite.AnyFunSuite
import team.starfish.lang.alt.AltTokenizer

import java.nio.charset.StandardCharsets
import scala.io.{Codec, Source}

class SamplesTest extends AnyFunSuite:

  test("print a surprise"):
    runAndAssert("surprise", "!")

  test("print a"):
    runAndAssert("printa", "a")

  test("hello world"):
    runAndAssert("helloworld", "Hello World!")

  test("echo"):
    runAndAssert("echo", "abc sdf 56", "abc sdf 56")

  test("test counter"):
    runAndAssert("counter_test", "oI")

  test("test repeating counter"):
    runAndAssert("repeating_counter_test", "ABCD")

  test("test comparisons"):
    runAndAssert("comparisons_test", "AB")

  test("switch case"):
    runAndAssert("switchcase", "Ss# ç", "sS# ç")

  test("rot13"):
    runAndAssert("rot13", "NM nf . egF", "AZ as . rtS")

  test("reverse"):
    val script = StarScriptGenerators("reverse")(Nil)
    runAndAssertWithInput(script, "321cba", "abc123")

//  test("game_of_life"):
//    runAndAssert("game_of_life", "", "")



  def runAndAssert(seaFile: String, expectedOutput: String, userInput: String = "") =
    val input = Source.fromResource(s"samples/$seaFile.sea")(using Codec(StandardCharsets.UTF_8)).getLines().mkString("\n")
    runAndAssertWithInput(input, expectedOutput, userInput)

  def runAndAssertWithInput(input: String, expectedOutput: String, userInput: String = "") =
    val sourceReader = (file: String) =>
      Source.fromResource(s"samples/$file").getLines().mkString("\n")

    val tokenizer = AltTokenizer(sourceReader)
    val output = parseAndRun(input, tokenizer, userInput, "none")
    assert(output == expectedOutput)

    testStarsWithDialect(BlandDialect, tokenizer, input, expectedOutput, userInput)
    testStarsWithDialect(BeautifulDialect, tokenizer, input, expectedOutput, userInput)



  def testStarsWithDialect(dialect: StarDialect, tokenizer: AltTokenizer, input: String,expectedOutput: String, userInput: String = "") =
    val tokens = tokenizer.tokenize(input)
    val converted = StarWriter(dialect).write(tokens)
//
//    dialect.identifierMap.toList.sortBy(_._1).foreach: i =>
//      println(s"${i._1} - ${i._2} ; ${i._2.length}")
//
    println(converted)

    val starTokenizer = MainSyntaxStarTokenizer(dialect)

//    val tokens2 = starTokenizer.tokenize(converted)
//    val converted2 = StarWriter(dialect).write(tokens2)
//    println(converted2)

    val outputFromStars = parseAndRun(converted, starTokenizer, userInput, "none")
    assert(outputFromStars == expectedOutput)


