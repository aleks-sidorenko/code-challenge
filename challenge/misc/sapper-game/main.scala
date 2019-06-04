object Solution {

  type Pos = (Int, Int)
  
  val mines: Set[Pos] = ???
  
  sealed trait Cell
  object Cell {
    case object Bomb extends Cell
    case object Unknown extends Cell
    case class Open(bombs: Int) extends Cell
  }
  
  
  sealed trait Action {
    def pos: Pos
  }
  
  object Action {
    case class Flag(pos: Pos) extends Action
    case class Open(pos: Pos) extends Action
  }
  
  case class Board(cells: Map[Pos, Cell]) {
    lazy val unknown: Set[Cell] = ???
    
    def action(act: Action): Board = ???
    
    lazy val state: GameState = ???
  }
  
  object Board {
    def apply(mines: Set[Pos], size: (Int, Int)) = ???
  }
  
  sealed trait GameState
  object GameState {
    case object Won extends GameState
    case object Lost extends GameState
    case object InProgress extends GameState
  }


  def input(): Action = ???
  
  def game(mines: Set[Pos], size: (Int, Int)): GameState = {
    def loop(board: Board): GameState = {
      val newBoard = board.action(input)
      newBoard.state match {
        case GameState.InProgress =>  loop(newBoard)
        case state => state
      }
    }
    loop(Board(mines, size))
  }
}