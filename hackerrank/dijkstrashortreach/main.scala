object Solution {
  
  import scala.collection._

  
  type NodeId = Int
  case class Node(id: NodeId)
  case class Edge(from: NodeId, to: NodeId, weight: Int)

  case class Graph(nodes: List[Node], edges: List[Edge], start: NodeId) {
  
  }
  
  def readInput(): List[Graph] = {
    val sc = new java.util.Scanner (System.in)
    var graphCount = sc.nextInt()
    
    val graphs = new Array[Graph](graphCount)

    for(i <- 0 until graphCount) {
      val (nodeCount, edgeCount) = (sc.nextInt, sc.nextInt)
      val nodes = for (j <- 1 to nodeCount) yield Node(j)

      val edges = new Array[Edge](edgeCount)
      for (k <- 0 until edgeCount) {
        val edge = Edge(sc.nextInt, sc.nextInt, sc.nextInt)
        edges(k) = edge
      }
      val start = sc.nextInt

      graphs(i) = Graph(nodes.toList, edges.toList, start)
    }

    graphs.toList
  }

  
  def main(args: Array[String]) {
    val graphs = readInput()
    
    graphs.foreach(g => println(g))
  }

}
