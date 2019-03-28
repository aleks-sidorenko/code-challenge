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
  
  type Case = Tree[Data]
  type Diameter = Int
    

  def readInput(): List[Case] = {
    val tree1: Tree[Int] = Node(1, Some(Leaf(2)), Some(Leaf(3)))
    val tree2: Tree[Int] = Node(4, Some(tree1), Some(Leaf(6)))
    tree1 :: tree2 :: Nil
  }

  sealed trait Solution {
    def solve(input: Case): Diameter
  }


  final class SolutionImpl extends Solution {

    final def solve(input: Case): Diameter = {
      val tree = input

      def diameter(tree: Option[Tree[Data]], current: Diameter): Diameter = {
        tree match {
          case Some(t) => 
            math.max(
              diameter(t.left, 0) + diameter(t.right, 0),
              diameter(t.left, current + 1) + diameter(t.right, current + 1)
            )
          case None => current
        }
      }

      diameter(Some(tree), 0)
    
    }
  }
  

  def main(args: Array[String]) = {
    val cases = readInput()
    val solution = new SolutionImpl()
    cases.foreach { c =>
      println(s"${solution.solve(c)}")
    }
  }
}
