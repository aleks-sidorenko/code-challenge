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

    // caching
    val cache = collection.mutable.HashMap.empty[(Int, Int, Int), Int]

    final def solve(): Int = {
      step(pairs, 0, Int.MinValue)
    }

    private def step(pairs: List[Pair], acc: Int, min: Int): Int = {

      cache.getOrElseUpdate(
        (pairs.length, acc, min),
        pairs match {
          case p :: ps =>
            val (a, b) = p
            val ignore = step(ps, acc, min)
            if (a < min) math.max(step(ps, 1, b), ignore)
            else math.max(step(ps, acc + 1, b), ignore)
          case _ => acc
        }
      )
    }

  }

  def main(args: Array[String]) = {
    val cases = readInput()

    cases.foreach { c =>
      println(Solution(c).solve())
    }
  }
}
