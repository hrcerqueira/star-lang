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

private val BeautifulIdentifiersMap = List(
  "#" -> "#\uFE0F⃣",
  "\"" -> "\uD83E\uDE87",
  "%" -> "\uD83D\uDD96",
  "<" -> "\uD83D\uDC08",
  ">" -> "\uD83D\uDC15",
  "$" -> "\uD83D\uDC20",
  "@" -> "\uD83D\uDC40",

  "a" -> "\u2708\uFE0F", // airplane
  "b" -> "\uD83C\uDF88\uFE0F", // balloon
  "c" -> "\uD83C\uDF35\uFE0F", // cactus
  "d" -> "\uD83D\uDC36\uFE0F", // dog
  "e" -> "\uD83E\uDD85\uFE0F", // eagle
  "f" -> "\uD83D\uDD25\uFE0F", // fire
  "g" -> "\uD83D\uDC7B\uFE0F", // ghost
  "h" -> "\uD83C\uDFE0\uFE0F", // house
  "i" -> "\uD83C\uDF66\uFE0F", // ice cream
  "j" -> "\uD83C\uDF83\uFE0F", // jack-o-lantern
  "k" -> "\uD83D\uDC28\uFE0F", // koala
  "l" -> "\uD83E\uDD81\uFE0F", // lion
  "m" -> "\uD83D\uDC12\uFE0F", // monkey
  "n" -> "\uD83D\uDC43\uFE0F", // nose
  "o" -> "\uD83D\uDC19\uFE0F", // octopus
  "p" -> "\uD83C\uDF55\uFE0F", // pizza
  "q" -> "\u2753\uFE0F", // question mark
  "r" -> "\uD83D\uDE80\uFE0F", // rocket
  "s" -> "\uD83D\uDC0D\uFE0F", // snake
  "t" -> "\uD83C\uDF2E\uFE0F", // taco
  "u" -> "\u2602\uFE0F", // umbrella
  "v" -> "\uD83C\uDFBB\uFE0F", // violin
  "w" -> "\uD83D\uDC0B\uFE0F", // whale
  "x" -> "\uD83C\uDF3B", // sunflower
  "y" -> "\uD83E\uDDF6\uFE0F", // yarn
  "z" -> "\uD83E\uDD93\uFE0F", // zebra

  "A" -> "\uD83D\uDE91\uFE0F", // ambulance
  "B" -> "\uD83E\uDD8B\uFE0F", // butterfly
  "C" -> "\uD83D\uDC08\uFE0F", // cat
  "D" -> "\uD83D\uDC2C\uFE0F", // dolphin
  "E" -> "\uD83D\uDC18\uFE0F", // elephant
  "F" -> "\uD83D\uDC38\uFE0F", // frog
  "G" -> "\uD83E\uDD92\uFE0F", // giraffe
  "H" -> "\uD83E\uDD94\uFE0F", // hedgehog
  "I" -> "\uD83E\uDDCA", // ice
  "J" -> "\uD83D\uDC56\uFE0F", // jeans
  "K" -> "\uD83D\uDD11\uFE0F", // key
  "L" -> "\uD83C\uDF4B\uFE0F", // lemon
  "M" -> "\uD83C\uDFCD\uFE0F", // motorcycle
  "N" -> "\uD83D\uDC85\uFE0F", // nail polish
  "O" -> "\uD83E\uDD89\uFE0F", // owl
  "P" -> "\uD83D\uDC27\uFE0F", // penguin
  "Q" -> "\u2754\uFE0F", // white question mark
  "R" -> "\uD83C\uDF39\uFE0F", // rose
  "S" -> "\u2600\uFE0F", // sun
  "T" -> "\uD83D\uDC2F\uFE0F", // tiger
  "U" -> "\uD83E\uDD84\uFE0F", // unicorn
  "V" -> "\uD83C\uDF0B\uFE0F", // volcano
  "W" -> "\uD83D\uDC3A\uFE0F", // wolf
  "X" -> "\uD83C\uDF30", // chestnut
  "Y" -> "\uD83E\uDD80\uFE0F", // yarn? Actually yak -> use yak
  "Z" -> "\uD83E\uDDDF\uFE0F", // zombie

).toMap


private val BeautifulDialect = StarDialect(
  tokenMap = Map(
    "⭐" -> STAR,
    "\uD83C\uDF1F" -> PADDING, // 🌟
    "\uD83D\uDD35" -> DECREMENT, // 🔵
    "\uD83D\uDFE0" -> INCREMENT, // 🟠
    "\uD83D\uDED1" -> DONE, // 🛑
    "💫" -> BEGIN_CONTROL, // 💫
    "\uD83D\uDD01" -> BEGIN_CONTROL_DIVISION, // 🔁
    "\uD83C\uDFC1" -> END_CONTROL, // 🏁
    "\uD83D\uDCDD" -> PRINT, // 📝
    "\uD83D\uDD0E" -> READ, // 🔎
  ),
  mainStar = "\uD83D\uDC20", // 🐠
  whiteSpace = "⚫",
  identifierMap = BeautifulIdentifiersMap,
)

val allDialects = List(
  BlandDialect,
  OkDialect,
  BeautifulDialect,
)