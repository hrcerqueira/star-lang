package io.hnrc.star.lang.utils

import scala.jdk.CollectionConverters.*
import scala.util.boundary

extension [A](option: Option[A])

  def tap(block: A => Unit) =
    option match
      case Some(a) => block(a)
      case None =>
    option


extension [T](obj: T)
  def let[R](block: T => R): R =
    block(obj)

  def also(block: T => Unit): T =
    block(obj)
    obj

extension (line: String)
  def utf8Chars: List[String] =
    line.codePoints()
      .mapToObj(Character.toChars)
      .map[String](new String(_))
      .toList
      .asScala
      // remove some utf8 garbage that java leaves behind
      .filter(c => c.length != 1 || !List(65039, 65038, 8419).contains(c.codePointAt(0)))
      .toList

object CharacterGenerator:
  private var lastGeneratedCodepoint = 12449 - 1 // start at ァ
  
  def nextCharacter =
    (lastGeneratedCodepoint + 1).let: newCodePoint =>
      lastGeneratedCodepoint = newCodePoint
      new String(Character.toChars(lastGeneratedCodepoint))
      

object MidPointIteratorFactory:

  private var lastComputedRadius = -1
  private var preComputedList: List[(Int, Int)] = Nil

  private def computeMore() =
    val points = computePointsForRadius(lastComputedRadius + 1)
      .filter: point =>
        !preComputedList.contains(point)
      .distinct

    lastComputedRadius += 1
    preComputedList = preComputedList ++ points

  private def computePointsForRadius(r: Int): List[(Int, Int)] =

    if r == 0 then
      return (0, 0) :: Nil

    var points = (r, 0) :: Nil

    var x = r
    var y = 0

    var P = 1 - r

    boundary:
      while x > y do
        y += 1

        if P <= 0 then
          P = P + 2 * y + 1
        else
          x -= 1
          P = P + 2 * y - 2 * x + 1

        if x < y then
          boundary.break()

        points = (x, y) :: points

        if x == y then
          points = (y, x) :: points


    points
      .flatMap:
        case (x, y) =>
          (x, y) :: (y, x) :: Nil
      .flatMap:
        case (x, y) =>
          (x, y) :: (-x, -y) :: (-x, y) :: (x, -y) :: Nil


  def iterator = new Iterator[(Int, Int)]:

    var indexInPrecomputedList = -1

    override def hasNext: Boolean = true

    override def next(): (Int, Int) =
      val nextIndex = indexInPrecomputedList + 1
      if nextIndex >= preComputedList.size then
        computeMore()
        next()
      else
        indexInPrecomputedList = nextIndex
        preComputedList(nextIndex)

