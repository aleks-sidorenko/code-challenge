object Solution {
  import scala.collection._

  def readInput(): List[String] = {
    val sc = new java.util.Scanner(System.in)

    val t = sc.nextInt
    val cases = new Array[String](t)
    for (i <- 0 until t) {
      val s = sc.next
      cases(i) = s
    }
    cases.toList
  }

  case class Solution(str: String) {
    final def solve(): String = {
      str
    }
  }

  def main(args: Array[String]) = {
    val cases = readInput()

    cases.foreach { c =>
      println(Solution(c).solve())
    }
  }
}
