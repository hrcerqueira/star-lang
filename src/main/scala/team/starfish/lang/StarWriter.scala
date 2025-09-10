package team.starfish.lang

import team.starfish.lang.StarMetaInstructions.{PADDING, STAR}

import scala.util.boundary
import boundary.break
import scala.collection.mutable

class StarWriter(private val dialect: StarDialect):

  private lazy val tokensToSymbol = dialect.tokenMap.toList.map(t => (t._2, t._1)).toMap

  def write(tokens: List[StarTokens]): String =
    val withCoordinates = tokens.filter(_.coordinates != null)
      .map(_.withBounds)

    val missingCoordinates = tokens.filter(_.coordinates == null).sortBy(_.legSize).reverse

    val allSet = setCoordinates(withCoordinates, missingCoordinates)

    val shiftY = allSet.map(_.minY).min.abs
    val shiftX = allSet.map(_.minX).min.abs

    val allSetNormalized = allSet.map: tnb =>
      tnb.tokens.copy(
        coordinates = StarCoordinates(
          tnb.coordinates.x + shiftX,
          tnb.coordinates.y + shiftY,
        )
      )

    val maxX = allSet.map(_.maxX).max + shiftX
    val maxY = allSet.map(_.maxY).max + shiftY

    val buffer = List.fill(maxY)(Array.fill(maxX)(dialect.whiteSpace))

    allSetNormalized.foreach(writeStar(buffer, _))

    buffer.map(_.mkString).mkString("\n")


  private def setCoordinates(
    withCoordinates: List[TokensAndBounds],
    missingCoordinates: List[StarTokens],
    pointsMap: mutable.Set[(Int, Int)] = mutable.Set(),
    preTested: mutable.Map[(Int, Int), Int] = mutable.Map()
  ): List[TokensAndBounds] =

    if pointsMap.isEmpty then
      withCoordinates.foreach: wc =>
        val points = pointsWithShadow(wc)
        pointsMap.addAll(points)

    def pointsWithShadow(tnb: TokensAndBounds) =
      val basePoints = generateStarPoints(tnb.tokens).map:
        case (x, y, symbol) => (x, y)

      basePoints.flatMap:
        case (x, y) => List(
          (x, y - 1), (x + 1, y - 1),
          (x, y), (x + 1, y)
        )
      .toSet
    
    def clashes(testPoints: Set[(Int, Int)]): Boolean =
      pointsMap.intersect(testPoints).nonEmpty

    def inPretested(px: Int, py: Int, legSize: Int): Boolean =
      preTested.contains((px, py)) && preTested((px, py)) <= legSize

    missingCoordinates match
      case Nil => withCoordinates
      case tokens :: rest =>

        boundary[List[TokensAndBounds]]:
          for ((x, y) <- spiralFrom(0, 0)) do

            if !inPretested(x, y, tokens.legSize) then

              val testTokens = tokens.copy(coordinates = StarCoordinates(x * 2, y)).withBounds
              val testPoints =  pointsWithShadow(testTokens)

              if !clashes(testPoints) then
                pointsMap.addAll(testPoints)
                break(setCoordinates(testTokens :: withCoordinates, rest, pointsMap, preTested))
              else
                preTested((x, y)) = tokens.legSize

          Nil


  private def spiralFrom(x0: Int, y0: Int): Iterator[(Int, Int)] = new Iterator[(Int, Int)] {
    private val dirs = Array((1, 0), (0, -1), (-1, 0), (0, 1))
    private var dirIdx = 0
    private var stepLen = 1
    private var stepsTakenInDir = 0
    private var segmentsAtThisLen = 0
    private var x = x0
    private var y = y0
    private var first = true

    override def hasNext: Boolean = true

    override def next(): (Int, Int) = {
      if (first) {
        first = false
        (x, y)
      } else {
        val (dx, dy) = dirs(dirIdx)
        x += dx
        y += dy
        stepsTakenInDir += 1

        if (stepsTakenInDir == stepLen) {
          stepsTakenInDir = 0
          dirIdx = (dirIdx + 1) & 3
          segmentsAtThisLen += 1
          if (segmentsAtThisLen == 2) {
            segmentsAtThisLen = 0
            stepLen += 1
          }
        }
        (x, y)
      }
    }
  }

  private def writeStar(buffer: List[Array[String]], tokens: StarTokens) =
    generateStarPoints(tokens).foreach:
      case (x, y, symbol) =>
        buffer(y)(x) = symbol

  private def generateStarPoints(tokens: StarTokens) =
    val coordinates = tokens.coordinates.nn

    (coordinates.x, coordinates.y, STAR.translatedSymbol) ::
      (coordinates.x, coordinates.y + 1, StarIdentifier(tokens.identifier).translatedSymbol) :: (
      tokens.north.mapWithOneBasedIndex(tokens.legSize / 2): (token, i) =>
        (coordinates.x, coordinates.y - i, token.translatedSymbol)) ++ (
      tokens.east.mapWithOneBasedIndex(tokens.legSize): (token, i) =>
        (coordinates.x + i, coordinates.y, token.translatedSymbol)
      ) ++ (
      tokens.southEast.mapWithOneBasedIndex(tokens.legSize / 2): (token, i) =>
        (coordinates.x + i, coordinates.y + 1 + i, token.translatedSymbol)
      ) ++ (
      tokens.southWest.mapWithOneBasedIndex(tokens.legSize / 2): (token, i) =>
        (coordinates.x - i, coordinates.y + 1 + i, token.translatedSymbol)
      ) ++ (
      tokens.west.mapWithOneBasedIndex(tokens.legSize): (token, i) =>
        (coordinates.x - i, coordinates.y, token.translatedSymbol)
      )


  private def addShadow(points: List[(Int, Int)], size: Int, trf: (Int, Int, Int) => (Int, Int)) =
    0.until(size).map: i =>
      points.map:
        case (x, y) => trf(x, y, i)

  extension (token: StarToken)
    private def translatedSymbol = token match
      case StarIdentifier(symbol) => dialect.identifierMap.getOrElse(symbol, symbol)
      case _ => tokensToSymbol(token)

  extension (leg: List[StarToken])
    private def mapWithOneBasedIndex(size: Int)(block: (StarToken, Int) => (Int, Int, String)) =
      leg.padTo(size, PADDING)
        .zipWithIndex
        .map: (token, i) =>
          block(token, i + 1)

case class TokensAndBounds(
  tokens     : StarTokens,
  legSize    : Int,
  bounds     : (StarCoordinates, StarCoordinates),
  coordinates: StarCoordinates
):
  def minX = bounds._1.x

  def maxX = bounds._2.x

  def minY = bounds._1.y

  def maxY = bounds._2.y

  def printBounds = println(s"x: ${tokens.coordinates.nn.x}, y: ${tokens.coordinates.nn.y}, minX: $minX, minY: $minY, maxX: $maxX, maxY: $maxY, legSize: $legSize")


extension (tokens: StarTokens)

  def legSize = List(
    tokens.north.size * 2,
    tokens.east.size,
    tokens.southEast.size * 2,
    tokens.southWest.size * 2,
    tokens.west.size
  ).max

  def withBounds: TokensAndBounds =
    assert(tokens.coordinates != null)
    val ls = legSize
    val minX = tokens.coordinates.x - ls - 1
    val maxX = tokens.coordinates.x + ls + 1
    val minY = tokens.coordinates.y - ls / 2 - 1
    val maxY = tokens.coordinates.y + 1 + ls / 2 + 1

    TokensAndBounds(
      tokens = tokens,
      legSize = legSize,
      bounds = (StarCoordinates(minX, minY), StarCoordinates(maxX, maxY)),
      coordinates = tokens.coordinates
    )
