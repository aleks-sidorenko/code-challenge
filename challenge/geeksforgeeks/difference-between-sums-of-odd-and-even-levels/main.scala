object Solution {
  import scala.collection._
  
  case class Node(v: Int, left: Option[Node] = None, right: Option[Node] = None)

  def input(): List[Node]= {
    Node(1, Some(Node(2)), Some(Node(3))) :: Nil
  }

  def solve(tree: Node): Int = {  
    def bfs(n: Node, level: Int): List[(Int, Int)] = (level, n.v) :: n.left.map(bfs(_, level + 1)).getOrElse(Nil) ++ n.right.map(bfs(_, level + 1)).getOrElse(Nil)

    val traversal = bfs(tree, 1)
    val (odd, even) = traversal.foldLeft(0 -> 0) { case ((odd, even), (lvl, v)) => 
      if (lvl % 2 == 0) (odd, even + v) else (odd + v, even)
    }
    
    odd - even

  }

  
  def main(args: Array[String]) = {
    val cases = input() 
    cases.foreach { c =>
      println(s"${solve(c)}")
    }
  }
}
