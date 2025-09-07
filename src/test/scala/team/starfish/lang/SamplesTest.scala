package team.starfish.lang

import org.scalatest.funsuite.AnyFunSuite
import team.starfish.lang.alt.AltTokenizer

import scala.io.Source

class SamplesTest extends AnyFunSuite:
  
  test("print a surprise"):
    runAndAssert("surprise", "!")

  test("print a"):
    runAndAssert("printa", "a")
    
  test("hello world"):
    runAndAssert("helloworld", "Hello World!")

  test("echo"):
    runAndAssert("echo", "abc sdf 56", "abc sdf 56")
  
  def runAndAssert(seaFile: String, expectedOutput: String, userInput: String = "") =
    val input = Source.fromResource(s"samples/$seaFile.sea").getLines().mkString("\n")
    val output = parseAndRun(input, AltTokenizer, userInput)
    assert(output == expectedOutput)
