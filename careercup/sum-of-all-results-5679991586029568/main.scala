object Solution {

  def readInput(): List[List[Int]] = {
    val sc = new java.util.Scanner(System.in)
    val n = sc.nextInt
    val strings = collection.mutable.ListBuffer.empty[List[Int]]
    for (i <- 0 until n) strings += sc.next().map(_.asDigit).toList
    strings.toList
  }

  def sum(numbers: List[Int]): Int = {
    def sumHelper(sumSoFar: Int, nums: List[Int], prev: Int = 0): Int = {
      import math._
      nums match {
        case head::tail => {
            val cur = prev * 10 + head
            sumHelper(sumSoFar - head, tail, 0) + sumHelper(sumSoFar + head, tail, 0) 
              + sumHelper(sumSoFar - cur, tail, cur)
              + sumHelper(sumSoFar + cur, tail, cur)
            
          }
        case Nil => abs(sumSoFar)
      }
    }

    sumHelper(0, numbers)

  }

  def main(args: Array[String]) {
    val strings = readInput

    strings.foreach(s => println(sum(s)))
  }
}
