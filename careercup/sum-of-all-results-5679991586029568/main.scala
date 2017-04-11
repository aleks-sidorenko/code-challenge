object Solution {

  def readInput(): List[List[Int]] = {
    val sc = new java.util.Scanner(System.in)
    val n = sc.nextInt
    val strings = collection.mutable.ListBuffer.empty[List[Int]]
    for (i <- 0 until n) strings += sc.next().map(_.asDigit).toList
    strings.toList
  }

  def sum(nums: List[Int]): Int = {
    import math._
    nums match {
      case head::tail => {
        val rest = sum(tail)
        abs(head + rest) + abs(-1*head + rest) + abs(head - rest) + abs(-1*head - rest)
        }
      case Nil => 0
    }
  }

  def main(args: Array[String]) {
    val strings = readInput

    
    strings.foreach(s => println(sum(s)))
  }
}
