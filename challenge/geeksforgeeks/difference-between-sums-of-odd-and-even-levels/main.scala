object Solution {
  import scala.collection._
  
  case class Node(v: Int, left: Option[Node] = None, right: Option[Node] = None)

  def input(): List[Node]= {
    Node(1, Node(2), Node(3)) :: Nil
  }

  def solve(tree: Node): Int = {  
    0
    
  }

  
  def main(args: Array[String]) = {
    val cases = input() 
    cases.foreach { c =>
      println(s"${solve(c)}")
    }
  }
}
