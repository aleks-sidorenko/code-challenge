object Solution {
  import scala.collection._

  type Data = Int

  sealed trait Tree[T] {
    def data: T
    def left: Option[Tree[T]]
    def right: Option[Tree[T]]
  }

  case class Node[T](data: T, right: Option[Tree[T]], left: Option[Tree[T]]) extends Tree[T]

  case class Leaf[T](data: T) extends Tree[T] {
    val left = None
    val right = None
  }
  
  type Case = (Tree[Int], (Data, Data))
  type Result = Int    
    

  def readInput(): List[Case] = {
    val tree1: Tree[Int] = Node(1, Some(Leaf(2)), Some(Leaf(3)))
    val tree2: Tree[Int] = Node(4, Some(tree1), Some(Leaf(6)))
    (tree1 -> (2, 3)) :: (tree2 -> (6, 3)) :: (tree2 -> (1, 3)) :: Nil
  }

  sealed trait Solution {
    def solve(input: Case): Result
  }


  class Solution1 extends Solution {

    final def solve(input: Case): Result = {
      val (tree, (a, b)) = input

      def findPath(tree: Tree[Data], d: Data, path: List[Data]): List[Data] = {
        if (tree.data == d) d :: path
        else {
          tree match {
            case Leaf(_) => Nil
            case Node(n, l, r) => 
              val left = l.map(findPath(_, d, n :: path)).getOrElse(Nil)
              val right = r.map(findPath(_, d, n :: path)).getOrElse(Nil)
              if (left.nonEmpty) left else if (right.nonEmpty) right else Nil
          }
        }
      }

      val pathA = findPath(tree, a, Nil)
      val pathB = findPath(tree, b, Nil)
      val all = pathA.toSet | pathB.toSet
      val diff = pathA.toSet & pathB.toSet
      (all &~ diff).size

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
