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
    
    private var exp: List[K] = Nil

    def expired = exp.reverse

    def get(k: K): Option[T] = {
      queue.get(k) match {
        case value @ Some(v) => 
          update(k, v)
          value
        case _ => None
      }
    }

    def put(k: K, v: T): Unit = {
      if (queue.contains(k)) {
        update(k, v)
      } else {
        queue.put(k, v)
        expire()
      }
    }

    private def expire() = {
      if (queue.size > limit) { 
        val (k, v) = queue.head
        queue.remove(k)
        exp = k :: exp
      }
    }

    private def update(k: K, v: T) = {
      queue.remove(k)
      queue.put(k, v)
    }
  }

  case class Solution(limit: Limit, input: List[Input]) {
    private val cache = new LruCache[String, String](limit)
    final def solve(): List[String] = {
      input.foreach { 
        case Put(k, v) => cache.put(k, v)
        case Get(k) => cache.get(k)
      }
      cache.expired
    }

  }

  def main(args: Array[String]) = {
    val cases = readInput()

    cases.foreach { case (l, actions) =>
      println(Solution(l, actions).solve())
    }
  }
}
