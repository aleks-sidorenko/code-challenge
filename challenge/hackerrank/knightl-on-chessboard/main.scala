object Solution {
  import scala.collection._

  def readInput(): Int = {
    val sc = new java.util.Scanner(System.in)
    sc.nextInt()
  }

  type Coord = (Int, Int)
  type Distance = Int

  final case class Board(n: Int, piece: Knight) {
    lazy val distances = initialDistances(piece.position)
    lazy val visited = new mutable.HashSet[Coord]()

    final def inside(pos: Coord): Boolean = {
      val (x, y) = pos
      x >= 0 && x < n && y >= 0 && y < n
    }

    private def next(): Option[Coord] = {
      val notVisited = distances.filter {
        case (k, v) => !visited(k) && v != Int.MaxValue
      }
      if (notVisited.isEmpty) None
      else Some(notVisited.minBy(_._2)._1)

    }

    private def finished(pos: Option[Coord]): Boolean =
      pos.isEmpty || pos.get == (n - 1, n - 1)
    private def distance(pos: Option[Coord]): Distance = pos match {
      case Some(p) => distances(p)
      case _       => -1
    }

    private def update(p: Piece) = {
      val pos = p.position
      val dist = distances(pos)
      val moves = p.nextMoves(this)
      moves.foreach { m =>
        if (dist + 1 < distances(m)) {
          distances(m) = dist + 1
        }
      }
      visited += pos

    }

    final def solve(): Distance = {
      val pos = next()

      if (finished(pos))
        distance(pos)
      else {
        val p = piece.copy(position = pos.get)
        update(p)
        solve()
      }
    }

    private def initialDistances(init: Coord): mutable.Map[Coord, Distance] = {
      val d = new mutable.HashMap[Coord, Distance]()
      for {
        i <- 0 until n
        j <- 0 until n

      } d((i, j)) = Int.MaxValue
      d(init) = 0
      d
    }
  }

  trait Piece {
    def position: Coord
    def nextMoves(board: Board): Iterable[Coord]
  }

  type KnightConfig = (Int, Int)

  final case class Knight(cfg: KnightConfig, position: Coord) extends Piece {
    override def nextMoves(board: Board): Iterable[Coord] = {
      val (x, y) = cfg
      val moves
        : List[Coord] = (-x, -y) :: (-x, y) :: (x, -y) :: (x, y) :: (-y, -x) :: (-y,
                                                                                 x) :: (y,
                                                                                        -x) :: (y,
                                                                                                x) :: Nil
      val unique = moves.toSet.toList
      unique.map { case (x, y) => (position._1 + x, position._2 + y) } filter {
        p =>
          board.inside(p)
      }
    }
  }

  case class Solution(n: Int) {

    def solve(): Iterable[Iterable[Distance]] = {
      // memoization
      var solved = Map.empty[KnightConfig, Distance]
      for {
        i <- 1 until n
      } yield {
        for {
          j <- 1 until n
        } yield {
          solved.get((i, j)) match {
            case Some(res) => res
            case _ =>
              val res = solveFor(i, j)
              solved = solved + ((i, j) -> res, (j, i) -> res)
              res

          }
        }
      }
    }

    def solveFor(cfg: KnightConfig): Distance = {
      val board = Board(n, Knight(cfg, (0, 0)))
      board.solve()
    }
  }

  def main(args: Array[String]) = {
    val n = readInput()
    val solution = Solution(n)
    solution.solve().foreach { line =>
      println(line.mkString(" "))
    }
  }
}
