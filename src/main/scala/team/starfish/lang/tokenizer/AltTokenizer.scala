package team.starfish.lang.tokenizer

import team.starfish.lang.*
import team.starfish.lang.tokenizer.StarInstruction.*
import team.starfish.lang.tokenizer.StarMetaInstructions.*
import team.starfish.lang.tokenizer.{BlandDialect, StarCoordinates, StarDialect, StarIdentifier, StarTokenList, StarTokenizer, StarTokens}
import team.starfish.lang.utils.{CharacterGenerator, let, utf8Chars}

import scala.::

class AltTokenizer(sourceReader: String => String = s => "") extends StarTokenizer:

  import AltTokenize.*

  val dialect: StarDialect = BlandDialect

  def tokenize(input: String): List[StarTokens] =
    tokenizeWithReplacements(input, Map())

  private def tokenizeWithReplacements(input: String, replacements: Map[String, String]): List[StarTokens] =
    val lines = input.split("\n")

    val included = readIncludedSeas(lines, replacements)

    val cleanedLines = lines
      .map(removeComments)
      .map: line =>
        line.replaceAll(" ", "")
      .map: line =>
        replacements.toList.foldLeft(line): (line, replacement) =>
          line.replaceAll(replacement._1, replacement._2)
      .toList

    (included ++ readStars(cleanedLines)).distinctBy(_.identifier).toList


  private def readIncludedSeas(lines: Array[String], originalReplacements: Map[String, String]) =
    lines.filter(_.startsWith(s"$commentChar#include "))
      .map(line => line.substring(line.indexOf(' ')))
      .map(_.trim.split(' ').map(_.trim))
      .flatMap: chunks =>
        val fileToInclude = chunks.head

        val replacements = chunks.drop(1)
          .map: chunk =>
            val chars = chunk.utf8Chars
            if chars.length < 1 then throw new RuntimeException(s"Invalid replacement at include statement: $chunk")
            val from = chars.head
            val to = (if chars.length == 1 then CharacterGenerator.nextCharacter else chars(1)).let: to =>
              originalReplacements.getOrElse(to, to)

            (from, to)
          .toMap

        val content = sourceReader(fileToInclude)

        tokenizeWithReplacements(content, replacements)


  private val starStarRegex = """^(\P{C})(\.(\d+)\.(\d+))?\s*$""".r

  private def removeComments(line: String): String =
    line.indexOf(commentChar(0)) match
      case -1 => line.trim
      case index => line.take(index).trim

  private def readStars(lines: List[String]): List[StarTokens] = lines match
    case Nil => Nil
    case line :: tail if line.isEmpty => readStars(tail)
    case starStarRegex(character, _, x, y) :: tail =>
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
        case '~' => DEBUG
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

  object AltTokenize:
    val commentChar = ";"
