import scala.collection.mutable

object Solution {

  def readInput(): String = {
    val sc = new java.util.Scanner(System.in)
    sc.nextLine()
  }

  def reduce(input: String): String = {
    val chars = new mutable.HashSet[Char]() ++ input

    input.filter { x =>
      val exists = chars(x)
      if (exists) chars -= x
      exists
    }
  }

  def main(args: Array[String]) {
    val input = readInput()
    val result = reduce(input)

    println(result)
  }
}