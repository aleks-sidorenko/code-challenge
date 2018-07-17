object Solution {
  import scala.collection._

  sealed trait Tree[T] {
    def data: T
    def left: Option[Tree[T]]
    def right: Option[Tree[T]]
  }

  case class Node[T](data: T, right: Option[Tree[T]], left: Option[Tree[T]])
      extends Tree[T]

  case class Leaf[T](data: T) extends Tree[T] {
    val left = None
    val right = None
  }
  type Data = Int

  type Case = Tree[Data]
  type Result = List[Data]

  def readInput(): List[Case] = {
    val tree1: Tree[Int] = Node(1, Some(Leaf(2)), Some(Leaf(3)))
    val tree2: Tree[Int] = Node(
      10,
      Some(Node(20, 
            Some(Node(40, Some(Leaf(60)), None)), None)),
      Some(Leaf(30)))
    tree1 :: tree2 :: Nil
  }

  case class Solution(tree: Tree[Data]) {

    final def solve(): List[Data] = {
      Nil
    }

  }

  def main(args: Array[String]) = {
    val cases = readInput()
    cases.foreach { c => 
      println(Solution(c).solve().mkString(" "))
    }
  }
}
