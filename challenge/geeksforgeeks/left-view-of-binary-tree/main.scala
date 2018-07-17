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
      Some(Node(20, Some(Node(40, Some(Leaf(60)), None)), None)),
      Some(Node(30, Some(Node(50, Some(Node(70, 
        Some(Leaf(90)), None)), None)), None))
      )
    tree1 :: tree2 :: Nil
  }

  case class Solution(tree: Tree[Data]) {
    type Level = Int

    final def solve(): List[Data] = {
      def loop(node: Option[Tree[Data]],
               view: Map[Level, Data],
               level: Level): Map[Level, Data] = {
        node match {
          case Some(Node(d, l, r)) =>
            view.get(level) match {
              case Some(_) =>
                loop(l, view, level + 1) ++ loop(r, view, level + 1)
              case None =>
                 loop(r, view + (level -> d), level + 1) ++ loop(l, view + (level -> d), level + 1)
            }
          case Some(Leaf(d)) =>
            view.get(level) match {
              case Some(_) => view
              case None    => view + (level -> d)
            }
          case None => view
        }
      }
      val res = loop(Some(tree), Map.empty[Level, Data], 0)

      SortedMap(res.toSeq: _*).values.toList
    }

  }

  def main(args: Array[String]) = {
    val cases = readInput()
    cases.foreach { c =>
      println(Solution(c).solve().mkString(" "))
    }
  }
}
