object Solution {
  import scala.collection._

  def readInput(): Int = {
    val sc = new java.util.Scanner(System.in)
    sc.nextInt
  }

  case class Solution(n: Int) {

    final def solve(): List[List[Int]] = {
      val res = (1 to n).foldLeft[List[List[Int]]](Nil) {
        case (acc, n) =>
          val prev = if (n == 1) List(1) else next(acc.head)
          prev :: acc
      }

      res.reverse
    }

    private def next(prev: List[Int]): List[Int] =
      1 :: (prev.zip(prev.tail).map { case (a, b) => a + b }) ::: (1 :: Nil)

  }

  def main(args: Array[String]) = {
    val n = readInput()
    val res = Solution(n).solve()
    res.map(_.mkString(" ")).foreach(println(_))
  }
}
