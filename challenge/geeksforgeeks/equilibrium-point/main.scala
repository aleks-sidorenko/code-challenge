object Solution {
  import scala.collection._


  def readInput(): List[List[Int]] = {
    val sc = new java.util.Scanner(System.in)

    val t = sc.nextInt
    val cases = new Array[List[Int]](t)
    for (i <- 0 until t) {
      val n = sc.nextInt
      val list = new Array[Int](n)
      for (j <- 0 until n) {
        list(j) = sc.nextInt
      }
      cases(i) = list.toList

    }

    cases.toList

  }

  case class Solution(numbers: List[Int]) {

    final def solve(): Option[Int] = {
      val sum = numbers.sum
      val (res, _) = numbers.zipWithIndex.foldLeft[(Option[Int], Int)](None -> 0) { case ((res, s), (n, i)) => 
        if (res.isDefined) res -> s
        else if (s == sum - n - s) Some(i) -> s
        else None -> (s + n)
      }

      res.map(_ + 1)
    }

  }

  def main(args: Array[String]) = {
    val cases = readInput()

    cases.foreach { c =>
      println(Solution(c).solve().getOrElse(-1))
    }
  }
}
