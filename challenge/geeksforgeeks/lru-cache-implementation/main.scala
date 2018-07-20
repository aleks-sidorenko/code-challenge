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

  class Queue[T] {
    class Node(val value: T,
               var prev: Option[Item] = None,
               var next: Option[Item] = None)

    private var front: Option[Item] = None
    private var rear: Option[Item] = None

    def isEmpty = front.isEmpty && rear.isEmpty

    def update(node: Node) = ???

    def enqueue(v: T) = {
      val n = new Node(v, None, rear)
      if (isEmpty) {
        front = rear = Some(n)
      } else {
        rear.prev = Some(n)
        rear = n
      }
    }

    def expire() = {
      if (isEmpty) {
        front = rear = None
      } else {
        front = front.prev
        if (front.isEmpty) front = rear
      }
    }

  }

  class LruCache[K, T](val limit: Int) {
    private val queue = new Queue[T]()
    private val map = new mutable.Map[K, Queue[T]#Node]

    def get(k: K): Option[T] = {
      val res = map.get(k)
      res match {
        case Some(_) =>
          queue.update(k)
          res
        case _ =>
          res
      }
    }

    def put(k: K, v: T): Option[K] = {
      if (limit < map.size) {
        val node = queue.enqueue(v)
        map += (k -> node)
      } else {
        val exp = queue.expire
        map.remove(exp.key)
      }
    }
  }

  case class Solution(numbers: List[Int]) {
    private val cache = new LruCache[Int]()
    final def solve(): Int = {
      0
    }

  }

  def main(args: Array[String]) = {
    val cases = readInput()

    cases.foreach { c =>
      println(Solution(c).solve())
    }
  }
}
