object Solution {
  
  import scala.collection._

  
  type NodeId = Int
  type Distance = Int
  case class Node(id: NodeId)
  
  case class Edge(from: NodeId, to: NodeId, distance: Distance)
  
  case class Graph(nodes: List[Node], edges: List[Edge]) {
    

    case class Connection(to: NodeId, distance: Distance)
    type DistMap = mutable.Map[NodeId, Distance]
    type VisitMap = mutable.Map[NodeId, Boolean]

    private final val initialDistance = Distance.MaxValue

    private val nodeMap = nodes.map(n => n.id -> n).toMap
    private val connectionMap = nodes.map(n => n.id -> connections(n.id)).toMap


    def distances(start: NodeId): List[Distance] = {
      val dist = dist(start, () => initialDistance)
      val toVisit = toVisit()
    
      val nodesToVisit = { nodeId: NodeId =>
        connectionMap(nodeId).map(_.to).filter(n => !visited(n)).toSet.toList
      }

      val processNode = { nodeId: NodeId =>
        var d = dist(nodeId)
        if (d == initialDistance) { d = 0; dist(nodeId) = d }
        val connections = connectionMap(nodeId)
        for (c <- connections) {
          if (dist(c.to) == initialDistance || (d + c.distance <  dist(c.to))) dist(c.to) = d + c.distance
        }
      }

      val nextNode = {
      
      }

      while (!toVisit.isEmpty) {
        val nodeId = queue.dequeue
        
        visited(nodeId) = true
        processNode(nodeId)
        queue ++= nodesToVisit(nodeId)
      }

      // take all except start
      dist.filter(_._1 != start).toSeq.sortBy(_._1).map(_._2).toList
    }

    private def connections(nodeId: NodeId): List[Connection] = {
      val c = edges.filter(e => e.from == nodeId).map(e => Connection(e.to, e.distance)) ++ 
        edges.filter(e => e.to == nodeId).map(e => Connection(e.from, e.distance))
      c.sortBy(_.distance)
    }

    private def dist[V](start: NodeId, default: () => V): mutable.Map[NodeId, V] = {
      val map = mutable.Map.empty[NodeId, V]
      nodes.foreach(n => map(n.id) = default())
      map(start) = 0
      map
    }

    private def toVisit() = {
      val set = mutable.Set.empty[NodeId]
      nodes.foreach(n => set += n.id)
      set
    }


  }

  case class TestCase(graph: Graph, start: NodeId)
  
  def readInput(): List[TestCase] = {
    val sc = new java.util.Scanner (System.in)
    var testCount = sc.nextInt()
    
    val tests = new Array[TestCase](testCount)

    for(i <- 0 until testCount) {
      val (nodeCount, edgeCount) = (sc.nextInt, sc.nextInt)
      val nodes = for (j <- 1 to nodeCount) yield Node(j)

      val edges = new Array[Edge](edgeCount)
      for (k <- 0 until edgeCount) {
        val edge = Edge(sc.nextInt, sc.nextInt, sc.nextInt)
        edges(k) = edge
      }
      val start = sc.nextInt

      tests(i) = TestCase(Graph(nodes.toList, edges.toList), start)
    }

    tests.toList
  }

  
  def main(args: Array[String]) {
    val tests = readInput()
    tests.foreach(t => println(t.graph.distances(t.start).mkString(" ")))
  }

}
