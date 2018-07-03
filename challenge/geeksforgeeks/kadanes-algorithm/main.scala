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

    final def solve(): Int = {

      def max(a: Int, b: Int, c: Int) = math.max(a, math.max(b, c))
      def initial = -math.abs(numbers.sum)

      def loop(acc: Int, from: Int, to: Int): Int = {
          if (to == numbers.length) acc
          else max(acc, loop(acc + numbers(to), from, to + 1), loop(numbers(to), to, to + 1))
      }
      loop(initial, 0, 0)
    }

  }

  def main(args: Array[String]) = {
    val cases = readInput()

    cases.foreach { c =>
      println(Solution(c).solve())
    }
  }
}
