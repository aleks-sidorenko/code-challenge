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

    private val cache = collection.mutable.HashMap.empty[(Int, Int, Int), Int]

    final def solve(): Int = {
      def loop(nums: List[Int], min: Int, acc: Int): Int =
        cache.getOrElseUpdate((nums.length, min, acc), {
          nums match {
            case x :: xs => 
              if (x < min) Math.max(loop(xs, x, acc + 1), loop(xs, min, acc))
              else Math.max(loop(xs, x, 0), loop(xs, min, acc))
            case _ => acc
          }
        })
      
      loop(numbers, Int.MaxValue, 0)
    }
    
    

  }

  def main(args: Array[String]) = {
    val cases = readInput()

    cases.foreach { c =>
      println(Solution(c).solve())
    }
  }
}
