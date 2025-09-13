package team.starfish.lang.tokenizer

sealed trait StarToken

case class StarCoordinates(x: Int, y: Int)

case class StarIdentifier(symbol: String) extends StarToken

enum StarMetaInstructions extends StarToken:
  case STAR, PADDING, DEBUG

enum StarInstruction extends StarToken:
  case INCREMENT, DECREMENT, PRINT, READ, DONE, BEGIN_CONTROL, BEGIN_CONTROL_DIVISION, END_CONTROL

type StarTokenList = List[StarToken]

case class StarTokens(
  identifier: String,
  north     : List[StarToken],
  east      : List[StarToken],
  southEast : List[StarToken],
  southWest : List[StarToken],
  west      : List[StarToken],
  coordinates: StarCoordinates | Null = null,
)

