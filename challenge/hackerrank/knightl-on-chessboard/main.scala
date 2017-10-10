object Main {
  import scala.collection._

  def readInput(): Int = {
      val sc = new java.util.Scanner (System.in)
      sc.nextInt()    
  }

  type Coord = (Int, Int)

  final case class Board(n: Int) { 
    private lazy val board = positions()

    final def positions(): Iterable[Coord] = {
      for { 
          i <- 0 until n
          j <- 0 until n
        } yield (i, j)
    }
    final def inside(pos: Coord): Boolean = {
      val (x, y) = pos
      x >= 0 && x < n && y >= 0 && y < n
    }
  }

  trait Piece {
    def board: Board
    def position: Coord    
    def move(to: Coord): Piece
    def nextMoves(): Iterable[Coord]
  }

  case class Solution(n: Int) {

    def solve(): Iterable[Iterable[Int]] = {
      List(List(1, 2, 3), 
      List(1, 2, 3), List(1, 2, 3))
    }
  }

  
  def main(args: Array[String]) {
    val n = readInput()  
    val solution = Solution(n)  
    solution.solve.foreach { line => println(line.mkString(" ")) }
  }
}
