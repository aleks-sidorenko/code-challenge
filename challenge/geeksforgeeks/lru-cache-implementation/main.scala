object Solution {
  import scala.collection._

  type Limit = Int
  sealed trait Input
  case class Put(k: String, v: String) extends Input
  case class Get(k: String) extends Input


  def readInput(): List[(Limit, List[Input])] = {
    val sc = new java.util.Scanner(System.in)

    val t = sc.nextInt
    val cases = new Array[(Limit, List[Input])](t)
    for (i <- 0 until t) {
      val l = sc.nextInt
      val n = sc.nextInt
      val input = new Array[Input](n)
      for (j <- 0 until n) {
         input(j) = sc.next match {
           case "get" => Get(sc.next)
           case "put" => Put(sc.next, sc.next)
         }
         
      }
      cases(i) = l -> input.toList

    }
    cases.toList
  }
  
  class LruCache[K, T](val limit: Limit) {
    private val queue = new mutable.LinkedHashMap[K, T]()

    def get(k: K): Option[T] = ???

    def put(k: K, v: T): Option[K] = ???
  }

  case class Solution(limit: Limit, input: List[Input]) {
    private val cache = new LruCache[Int, String](limit)
    final def solve(): List[Int] = Nil

  }

  def main(args: Array[String]) = {
    val cases = readInput()

    cases.foreach { case (l, actions) =>
      println(Solution(l, actions).solve())
    }
  }
}
