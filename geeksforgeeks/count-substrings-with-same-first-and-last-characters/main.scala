import scala.collection.immutable._

object Solution {

  def readInput(): List[String] = {
    val sc = new java.util.Scanner(System.in)
    val n = sc.nextInt()
    
    (for (i <- 0 until n) yield sc.next()).toList
  }

  def factorial(n: Int): Int = n match {
    case 0 => 1
    case _ => n * factorial(n-1)
  }

  def combinations(n: Int) = factorial(n) / (2 * factorial(n - 2))

  def substringsCount(str: String): Int = {
    val strings = toFreq(str)
    strings.values.fold(0)((acc, v) => {
      val extra = if (v == 1) 1 else v + combinations(v)
      acc + extra
    })
  }


  def toFreq(str: String): Map[Char, Int] = {
    str.groupBy(identity).mapValues(_.length)
  }

  def main(args: Array[String]) {
    val input = readInput()
    input.foreach { s => println(substringsCount(s)) }
  }
}