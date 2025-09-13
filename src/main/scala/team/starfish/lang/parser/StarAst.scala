package team.starfish.lang.parser

sealed trait StarNode

type StarNodeList = List[StarNode]

object Increment extends StarNode
object Decrement extends StarNode
object Print extends StarNode
object Read extends StarNode
object Done extends StarNode
object Debug extends StarNode

sealed trait StarControlNode extends StarNode:
  val nodes: StarNodeList

case class IfReferenceNotZero(
  star: String,
  nodes: StarNodeList,
) extends StarControlNode

case class IfSelfFullSpin(
  nodes: StarNodeList,
) extends StarControlNode

case class IfSelfNotZero(
  nodes: StarNodeList,
) extends StarControlNode

case class JumpToStar(
  star: String,
) extends StarNode

case class StarFish(
  symbol: String,
  north: StarNodeList,
  east: StarNodeList,
  southEast: StarNodeList,
  southWest: StarNodeList,
  west: StarNodeList,
)

case class Sea(
  starfishes: List[StarFish],
)