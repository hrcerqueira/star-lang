package team.starfish.lang

import org.scalatest.funsuite.AnyFunSuite
import team.starfish.lang.alt.AltTokenizer

class AltTokenizerTest extends AnyFunSuite:
  
  test("tokenize one star"):
    val input =
      """
        |f 10 10
        |[.]!
        |+++""".stripMargin
      
    val stars = AltTokenizer.tokenize(input)
    assert(stars.size == 1)
    assert(stars.head.north.size == 4)
    assert(stars.head.east.size == 3)
    assert(stars.head.southEast.isEmpty)
    assert(stars.head.southWest.isEmpty)
    assert(stars.head.west.isEmpty)
    
  test("tokenize multiple stars"):
    val input =
      """
        |1
        |+
        |
        |2
        |-
        |""".stripMargin
      
    val stars = AltTokenizer.tokenize(input)
    assert(stars.size == 2)
