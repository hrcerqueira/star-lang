package team.starfish.lang.alt

import team.starfish.lang.{BlandDialect, StarCoordinates, StarDialect, StarIdentifier, StarToken, StarTokenList, StarTokenizer, StarTokens}
import team.starfish.lang.StarInstruction.*

import scala.::

object AltTokenizer extends StarTokenizer:

  val dialect: StarDialect = BlandDialect

  def tokenize(input: String): List[StarTokens] =
    val lines = input.split("\n")
      .map(removeComments)
    readStars(lines.toList)

  private val starLineRegex = """^(\P{C})( (\d+) (\d+))?\s*$""".r

  private def removeComments(line: String): String =
    line.indexOf('#') match
      case -1 => line.trim
      case index => line.take(index).trim

  private def readStars(lines: List[String]): List[StarTokens] = lines match
    case Nil => Nil
    case line :: tail if line.isEmpty => readStars(tail)
    case starLineRegex(character, _, x, y) :: tail =>
      val (legs, restOfLines) = readLegs(tail)
      val otherStars = readStars(restOfLines)
      makeStar(character.charAt(0), x, y, legs) :: otherStars


  private def readLegs(lines: List[String]): (List[StarTokenList], List[String]) = lines match
    case line :: tail if line.isEmpty => (Nil, tail)
    case Nil => (Nil, Nil)
    case line :: tail =>
      val (otherLegs, restOfLines) = readLegs(tail)
      (readLeg(line) :: otherLegs, restOfLines)


  private def readLeg(line: String): StarTokenList =
    readLeg(line.toList)

  private def readLeg(line: List[Char]): StarTokenList = line match
    case Nil => Nil
    case ws :: tail if Character.isWhitespace(ws) => readLeg(tail)
    case tokenChar :: tail =>
      val token = tokenChar match
        case '[' => BEGIN_CONTROL
        case '{' => BEGIN_CONTROL_DIVISION
        case ']' => END_CONTROL
        case '+' => INCREMENT
        case '-' => DECREMENT
        case '.' => PRINT
        case '?' => READ
        case '!' => DONE
        case _ => StarIdentifier(tokenChar.toString)
      token :: readLeg(tail)


  private def makeStar(name: Char, x: String | Null, y: String | Null, legs: List[StarTokenList]): StarTokens =
    if legs.size > 5 then throw new RuntimeException("Too many legs")

    val coordinates = if x == null || y == null then null else StarCoordinates(x.toInt, y.toInt)

    def legAt(index: Int) = if (legs.size > index) legs(index) else Nil

    StarTokens(
      identifier = name.toString,
      coordinates = coordinates,
      north = legAt(0),
      east = legAt(1),
      southEast = legAt(2),
      southWest = legAt(3),
      west = legAt(4),
    )
