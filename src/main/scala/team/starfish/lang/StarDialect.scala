package team.starfish.lang

import team.starfish.lang.StarInstruction._
import team.starfish.lang.StarMetaInstructions._

case class StarDialect(
  tokenMap: Map[String, StarToken],
  mainStar: String
)

private val BlandDialect = StarDialect(
  tokenMap = Map(
    "*" -> STAR,
    "○" -> PADDING,
    "[" -> BEGIN_CONTROL,
    "{" -> BEGIN_CONTROL_DIVISION,
    "]" -> END_CONTROL,
    "+" -> INCREMENT,
    "-" -> DECREMENT,
    "." -> PRINT,
    "?" -> READ,
    "!" -> DONE,
  ),
  mainStar = "$"
)

private val BeautifulDialect = StarDialect(
  tokenMap = Map(
    "⍟" -> STAR,
    "○" -> PADDING,
    "⊕" -> INCREMENT, // 👍
    "⊖" -> DECREMENT, // 👎
    "✴" -> DONE,
    "↬" -> BEGIN_CONTROL, // 💫
    "↫" -> BEGIN_CONTROL_DIVISION, // ✨
    "⚝" -> END_CONTROL,
    "★" -> PRINT,
    "?" -> READ,
  ),
  mainStar = "\uD83D\uDC20"
)

private val BeautifulIdentifiersMap = (
  (BlandDialect.mainStar -> BeautifulDialect.mainStar) :: (
    0.until(26).map(i => (('a' + i).toString, ('ⓐ' + i).toString)) ++
      0.until(26).map(i => (('A' + i).toString, ('Ⓐ' + i).toString))
    ).toList
  ).toMap

