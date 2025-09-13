package team.starfish.lang.parser

import team.starfish.lang.tokenizer.StarInstruction.*
import team.starfish.lang.tokenizer.StarMetaInstructions.DEBUG
import team.starfish.lang.tokenizer.{StarIdentifier, StarToken, StarTokenList, StarTokens}

object StarParser:

  def parse(starTokensList: List[StarTokens]): Sea =
    val starfishes = starTokensList.map: starTokens =>
      StarFish(
        symbol = starTokens.identifier,
        north = starTokens.north.toAst,
        east = starTokens.east.toAst,
        southEast = starTokens.southEast.toAst,
        southWest = starTokens.southWest.toAst,
        west = starTokens.west.toAst
      )
    Sea(starfishes)
    
  extension (starTokens: StarTokenList)
    private def toAst: StarNodeList = consumeTokens._1
        
    private def consumeTokens: (StarNodeList, List[StarToken]) =
      starTokens match
        case Nil => (Nil, Nil)
        case INCREMENT :: tail => tail addNodeAndConsume Increment
        case DECREMENT :: tail => tail addNodeAndConsume Decrement
        case PRINT :: tail => tail addNodeAndConsume Print
        case READ :: tail => tail addNodeAndConsume Read
        case DONE :: tail => tail addNodeAndConsume Done
        case DEBUG :: tail => tail addNodeAndConsume Debug
        
        case BEGIN_CONTROL_DIVISION :: tail =>
          val (controlNodes, remainingTokens) = tail.consumeTokens
          val node = IfSelfFullSpin(controlNodes)
          remainingTokens addNodeAndConsume node

        case BEGIN_CONTROL :: StarIdentifier(identifier) :: tail =>
          val (controlNodes, remainingTokens) = tail.consumeTokens
          val node = IfReferenceNotZero(identifier, controlNodes)
          remainingTokens addNodeAndConsume node

        case BEGIN_CONTROL :: tail =>
          val (controlNodes, remainingTokens) = tail.consumeTokens
          val node = IfSelfNotZero(controlNodes)
          remainingTokens addNodeAndConsume node

        case StarIdentifier(identifier) :: tail => tail addNodeAndConsume JumpToStar(identifier)
          
        case END_CONTROL :: tail => (Nil, tail)

        case _ :: tail => tail.consumeTokens
        
    private infix def addNodeAndConsume(node: StarNode): (StarNodeList, List[StarToken]) =
      val (remainingNodes, remainingTokens) = consumeTokens
      if node == Done 
      then (Done :: Nil, remainingTokens)
      else (node :: remainingNodes, remainingTokens)  
  


