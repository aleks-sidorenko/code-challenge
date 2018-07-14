object Solution {
  import scala.collection._
  import Queue._

  def readInput(): List[Operation] = {
    val sc = new java.util.Scanner(System.in)

    val t = sc.nextInt
    val ops = new Array[Operation](t)
    for (i <- 0 until t) {
      val n = sc.nextInt
      val op = n match {
        case 1 => Enqeue(sc.nextInt)
        case 2 => Dequeue
        case 3 => Print
      }

      ops(i) = op

    }

    ops.toList

  }

  object Queue {
    sealed trait Operation
    case class Enqeue[T](x: T) extends Operation
    case object Dequeue extends Operation
    case object Print extends Operation

  }
  import Queue._

  class Queue[T] {
    private val a = new mutable.Stack[T]
    private val b = new mutable.Stack[T]

    def enqueue(x: T): T = {
      a.push(x)
      x
    }

    def dequeue: Option[T] = {
      if (!b.isEmpty) Some(b.pop)
      else {
        move()
        if (b.isEmpty) None
        else Some(b.pop)
      }
    }

    def print: Option[T] = {
      dequeue.map(enqueue(_))
    }

    private def move(): Unit = {
      while (!a.isEmpty) {
        b.push(a.pop)
      }
    }
  }

  case class Solution(ops: List[Queue.Operation]) {

    private val queue = new Queue[Int]

    final def solve(): List[Int] = {
      ops.foldLeft(List.empty[Int]) {
        case (acc, o) =>
          o match {
            case Enqeue(x: Int) =>
              queue.enqueue(x)
              acc
            case Dequeue =>
              queue.dequeue
              acc
            case Print =>
              queue.print.map { _ :: acc }.getOrElse(acc)
          }
      }
    }

  }

  def main(args: Array[String]) = {
    val ops = readInput()
    Solution(ops).solve().foreach(println(_))

  }
}
