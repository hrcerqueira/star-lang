package team.starfish.lang.tokenizer

import team.starfish.lang.*
import team.starfish.lang.utils.*

trait StarTokenizer:
  val dialect: StarDialect
  def tokenize(input: String): List[StarTokens]

class MainSyntaxStarTokenizer(val dialect: StarDialect, strict: Boolean = true) extends StarTokenizer:

  def tokenize(input: String): List[StarTokens] =
    val charMap = input
      .replaceAll(dialect.whiteSpace, " ")
      .asCoordinateMap

    charMap
      .filter(_._2 == starToken)
      .keys.toList
      .flatMap(tokenizeStar(_, charMap))

  private lazy val starToken = dialect.tokenMap.find(_._2 == StarMetaInstructions.STAR).map(_._1).get

  private def tokenizeStar(coordinates: StarCoordinates, charMap: CharMap): Option[StarTokens] =
    charMap.get(coordinates.south)
      .tap: identifier =>
        if dialect.tokenMap.contains(identifier) then throw new RuntimeException(s"Invalid identifier in position $coordinates")
      .map: identifier =>
        val startTokens = StarTokens(
          identifier = identifier,
          north = readLeg(coordinates.north, charMap, next = _.north),
          west = readLeg(coordinates.west, charMap, next = _.west),
          southWest = readLeg(coordinates.south.southWest, charMap, next = _.southWest),
          southEast = readLeg(coordinates.south.southEast, charMap, next = _.southEast),
          east = readLeg(coordinates.east, charMap, next = _.east),
        )
        if strict &&
          (startTokens.north.length != startTokens.southWest.length ||
          startTokens.north.length != startTokens.southEast.length ||
          startTokens.north.length != startTokens.west.length / 2 ||
          startTokens.north.length != startTokens.east.length / 2) then
          println(s"Star legs are not balanced in position $coordinates, identifier $identifier")
//          throw new RuntimeException(s"Star legs are not balanced in position $coordinates, identifier $identifier")

        startTokens


  private def readLeg(coordinate: StarCoordinates, charMap: CharMap, next: StarCoordinates => StarCoordinates): List[StarToken] =
    charMap.get(coordinate) match
      case Some(value) => toToken(value) :: readLeg(next(coordinate), charMap, next)
      case None => Nil


  private def toToken(char: String): StarToken =
    dialect.tokenMap.get(char).match
      case Some(value) => value
      case None => StarIdentifier(char)


private type CharMap = Map[StarCoordinates, String]

extension (input: String)
  def asCoordinateMap: CharMap = input.split("\n")
    .zipWithIndex
    .flatMap:
      case (line, lineNum) =>
        val utf8chars = line.utf8Chars

        utf8chars
          .zipWithIndex
          .map:
            case (" ", charNum) => null
            case (char, charNum) => (StarCoordinates(charNum, lineNum), char)
          .filter(_ != null)
          .map(_.nn)
    .toMap

extension (c: StarCoordinates)
  def south: StarCoordinates = StarCoordinates(c.x, c.y + 1)

  def north: StarCoordinates = StarCoordinates(c.x, c.y - 1)

  def east: StarCoordinates = StarCoordinates(c.x + 1, c.y)

  def west: StarCoordinates = StarCoordinates(c.x - 1, c.y)

  def southWest: StarCoordinates = StarCoordinates(c.x - 1, c.y + 1)

  def southEast: StarCoordinates = StarCoordinates(c.x + 1, c.y + 1)