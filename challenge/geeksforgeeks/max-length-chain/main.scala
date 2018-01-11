object Solution {
  import scala.collection._


  type Pair = (Int, Int)


  def readInput(): List[List[Pair]] = {
    val sc = new java.util.Scanner(System.in)
    
    
    val t = sc.nextInt
    val cases = new Array[List[Pair]](t)
    for (i <- 0 until t) {
      val n = sc.nextInt
      val pairs = new Array[Pair](n)
      for (j <- 0 until n) {
        pairs(j) = (sc.nextInt, sc.nextInt)
      }

      cases(i) = pairs.toList
      
    }

    cases.toList
    
  }


  case class Solution(pairs: List[Pair]) {
    final def solve(): Int = {
      pairs.length
    }

  }

  def main(args: Array[String]) = {
    val cases = readInput()

    cases.foreach { c =>
      println(s"$c")
      println(Solution(c).solve())
    }    
  }
}
