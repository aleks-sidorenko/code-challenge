object Solution {
  import scala.collection._

  sealed trait Tree {
    def left: Tree
    def right: Tree
  }

  case class Node(data: Int, right: Tree, left: Tree) extends Tree

  case class Leaf(data: Int) extends Tree {
    val left = Empty
    val right = Empty
  }

  case object Empty extends Tree {
    val left = Empty
    val right = Empty
  }

  type Case = (Tree, (Int, Int))
  type Result = Int

  def readInput(): List[Case] = {
    (Node(1, Leaf(2), Leaf(3)) -> (2, 3)) :: (Node(1,
                                                   Node(2, Leaf(4), Leaf(5)),
                                                   Leaf(3)) -> (4, 5)) :: Nil
  }

  sealed trait Solution {
    def solve(input: Case): Result
  }

  class Solution1 extends Solution {

    final def solve(input: Case): Result = {
      val (tree, (a, b)) = input

      def go(tree: Tree, acc: Int): Int = {
        tree match {
          case Leaf(d) if l == d || d == b => acc + 1
          case Node(d, l, r)               =>
          case Empty                       => acc
        }
      }

      go(tree, 0)
    }

  }

  def main(args: Array[String]) = {
    val cases = readInput()
    val solution = new Solution1()
    cases.foreach { c =>
      println(s"${solution.solve(c)}")
    }
  }
}
