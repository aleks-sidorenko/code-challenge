object Solution {
  import scala.collection._

  type Case = List[Int]
  def readInput(): List[Case] = {
    val sc = new java.util.Scanner(System.in)

    val t = sc.nextInt
    val cases = new Array[Case](t)
    for (i <- 0 until t) {
      val n = sc.nextInt
      val input = new Array[Int](n)
      for (j <- 0 until n) {
        input(j) = sc.nextInt
      }

      cases(i) = input.toList

    }

    cases.toList

  }

  sealed trait Solution {
    def solve(input: Case): (Int, Int)
  }

  class SortingSolution extends Solution {

    final def solve(input: Case): (Int, Int) = {
      val sorted = input.sorted
      val (missing, ind) = sorted.zipWithIndex.filter {
        case (n, i) => n != i + 1
      }.head

      (missing, ind + 1)
    }

  }

  def main(args: Array[String]) = {
    val cases = readInput()
    val solution = new SortingSolution()
    cases.foreach { c =>
      val (a, b) = solution.solve(c)
      println(s"$a $b")
    }
  }
}
