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

    private final val initialDistance = Int.MaxValue

    private val nodeMap = nodes.map(n => n.id -> n).toMap
    private val connectionMap = nodes.map(n => n.id -> connections(n.id)).toMap


    def distances(start: NodeId): List[Distance] = {
      val dist = toDist(start, initialDistance)
      val visit = toVisit()
    
      val processNode = { nodeId: NodeId =>
        var d = dist(nodeId)
        val connections = connectionMap(nodeId)
        for (c <- connections) {
          if (visit(c.to) && d + c.distance < dist(c.to)) dist(c.to) = d + c.distance
        }
      }

      val nextNode = () => {
        val nodeId = visit.minBy(dist(_))
        if (dist(nodeId) < initialDistance) {
          visit -= nodeId
          Some(nodeId)
        }
        else None
      }

      val distNoStart = () => dist.filter(_._1 != start).toSeq.sortBy(_._1).map(d => if (d._2 == initialDistance) -1 else d._2).toList

      var nodeId = nextNode()
      while (!visit.isEmpty && nodeId.isDefined) {
        processNode(nodeId.get)
        nodeId = nextNode()
      }

      // take all except start
      distNoStart()
      
    }

    private def connections(nodeId: NodeId): List[Connection] = {
      edges.filter(e => e.from == nodeId).map(e => Connection(e.to, e.distance)) ++ 
        edges.filter(e => e.to == nodeId).map(e => Connection(e.from, e.distance))
    }

    private def toDist(start: NodeId, default: Distance): DistMap = {
      val map = mutable.Map.empty[NodeId, Int]
      nodes.foreach(n => map(n.id) = default)
      map(start) = 0
      map
    }

    private def toVisit() = {
      val set = mutable.Set[NodeId]()
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
