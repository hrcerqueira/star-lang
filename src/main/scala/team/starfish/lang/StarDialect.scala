package team.starfish.lang

import team.starfish.lang.StarInstruction._
import team.starfish.lang.StarMetaInstructions._

case class StarDialect(
  tokenMap     : Map[String, StarToken],
  mainStar     : String,
  whiteSpace   : String = " ",
  identifierMap: Map[String, String] = Map.empty,
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

private val OkIdentifiersMap = (
  (BlandDialect.mainStar -> "\uD83D\uDC20") :: (
    0.until(26).map(i => (('a' + i).toString, ('ⓐ' + i).toString)) ++
      0.until(26).map(i => (('A' + i).toString, ('Ⓐ' + i).toString))
    ).toList
  ).toMap


private val OkDialect = StarDialect(
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
  mainStar = "\uD83D\uDC20",
  identifierMap =
    OkIdentifiersMap,
)

private val bloodACodePoint = "\uD83D\uDC08️".codePoints().findFirst().getAsInt

private val lowerCaseEmojiLetters = 0.until(26)
  .map: i =>
    ('a' + i).toChar.toString -> Character.toChars(bloodACodePoint + i).mkString
  .toList

private val smileCodePoint = "\uD83D\uDE42".codePoints().findFirst().getAsInt

private val upperCaseEmojiLetters = 0.until(26)
  .map: i =>
    ('A' + i).toChar.toString -> Character.toChars(smileCodePoint + i).mkString
  .toList

private val BeautifulIdentifiersMap = (
  (BlandDialect.mainStar -> "\uD83D\uDC20") ::
    ("#" -> "#\uFE0F⃣") ::
    ("\"" -> "\uD83C\uDDF5\uD83C\uDDF9") ::
    ("%" -> "\uD83C\uDDE9\uD83C\uDDEA") ::
    ("<" -> "\uD83D\uDC08") ::
    (">" -> "\uD83D\uDC15") ::
    (lowerCaseEmojiLetters ++ upperCaseEmojiLetters)
  ).toMap


private val BeautifulDialect = StarDialect(
  tokenMap = Map(
    "⭐" -> STAR,
    "\uD83C\uDF1F" -> PADDING, // 🌟
    "\uD83D\uDC4D" -> INCREMENT, // 👍
    "\uD83D\uDC4E" -> DECREMENT, // 👎
    "\uD83D\uDED1" -> DONE, // 🛑
    "💫" -> BEGIN_CONTROL, // 💫
    "\uD83D\uDD01" -> BEGIN_CONTROL_DIVISION, // 🔁
    "\uD83C\uDFC1" -> END_CONTROL, // 🏁
    "✍\uFE0F" -> PRINT, // ✍️
    "\uD83D\uDD0E" -> READ, // 🔎
  ),
  mainStar = "\uD83D\uDC20", // 🐠
  whiteSpace = "⚫",
  identifierMap = BeautifulIdentifiersMap,
)
