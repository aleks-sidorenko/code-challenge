

object Solution {
  case class Node(v: Int, left: Option[Node], right: Option[Node])
  def solution(tree: Node, k: Int): List[Node] = {
        
    def loop(node: Option[Node], i: Int): List[Node] = {
      node match {
        case Some(n) if i == 0 => List(n)
        case Some(n) => loop(n.left, i - 1) ::: loop(n.right, i - 1)
        case _ => Nil
      }
    }
    loop(Some(tree), k)
  }
}

object Main {

  import Solution._

  def main(args: Array[String]): Unit = {

    val tree = Node(
      1,
      Some(Node(
        2,
        None, 
        Some(Node(
          3, 
          Some(Node(4, None, None)), 
          None))
      )), 
      Some(Node(5, Some(Node(6, None, None)), None))
    )
    println(solution(tree, 1).map(_.v))
  }
}