object Solution {
  import scala.collection._

  type Data = Int

  sealed trait Tree[T] {
    def data: T
    def left: Option[Tree[T]]
    def right: Option[Tree[T]]
  }

  case class Node[T](data: T, left: Option[Tree[T]], right: Option[Tree[T]]) extends Tree[T]

  case class Leaf[T](data: T) extends Tree[T] {
    val left = None
    val right = None
  }
  
  type Case = Tree[Data]
  type Diameter = Int
  type Depth = Int
    

  def readInput(): List[Case] = {
    val tree1: Tree[Int] = Node(1, Some(Leaf(2)), Some(Leaf(3)))
    val tree2: Tree[Int] = Node(4, Some(tree1), Some(Leaf(6)))
    val tree3: Tree[Int] = Node(7, Some(tree2), Some(Leaf(8)))
    val tree4: Tree[Int] = Leaf(1)
    tree1 :: tree2 :: tree3 :: tree4 :: Nil
  }

  sealed trait Solution {
    def solve(input: Case): Diameter
  }


  final class SolutionImpl extends Solution {

    final def solve(input: Case): Diameter = {
      val tree = input

      val cache = collection.mutable.HashMap.empty[Data, Depth]

      def depth(node: Option[Tree[Data]], acc: Depth): Depth = {        
        node match {
          case Some(n) =>
            cache.getOrElseUpdate(n.data, math.max(depth(n.left, acc + 1), depth(n.right, acc + 1)))
          case _ => 
            acc - 1
        }
      }

      def diameter(tree: Option[Tree[Data]], root: Tree[Data], d: Diameter): Diameter = {        
        tree match {
          case Some(t) if t == root => 
            math.max(
              depth(t.left, 0) + depth(t.right, 0) + 2,
              math.max(diameter(t.left, t, 0), diameter(t.right, t, 0))
              )
          case Some(t) => 
            d + 1 + math.max(depth(t.left, 0), depth(t.right, 0))
          case None => 
            d
        }
      }

      diameter(Some(tree), tree, 0)
    
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
