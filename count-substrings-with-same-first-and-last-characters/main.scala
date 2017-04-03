import scala.collection.immutable._

object Solution {

  def readInput(): List[String] = {
    val sc = new java.util.Scanner(System.in)
    val n = sc.nextInt()
    
    (for (i <- 0 until n) yield sc.next()).toList
  }

  def substringsCount(str: String): Int = {
    val strings = substrings(str)    
    strings.filter(s => s(0) == s(s.length - 1)).size
  }


  def substrings(str: String): List[String] = {
    if (str.length == 1) return List(str)

    val cur = (for (i <- 1 to str.length) yield str.substring(0, i)).toList
    cur ::: substrings(str.tail) 
  }

  def main(args: Array[String]) {
    val input = readInput()
    input.foreach { s => println(substringsCount(s)) }
  }
}