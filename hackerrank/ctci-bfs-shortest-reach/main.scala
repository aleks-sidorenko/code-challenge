import scala.collection._
    
object Solution {

    case class Graph(nodeCount: Int, edgeCount: Int, root: GraphNode) {
        private final val Initial = -1
        private final val Ratio = 6
        
        private val dist = Array.fill(nodeCount)(Initial)
        private val visited = new mutable.HashSet[Int]()
        
        def distances: Seq[Int] = {
            
            visit()
            val result = dist.take(root.index) ++ dist.drop(root.index + 1)
            result.map {d => if (d != Initial) d * Ratio else d }
        }
        

        private def set(node: GraphNode, path: Int): Unit = {
            if (dist(node.index) == Initial) dist(node.index) = path

            dist(node.index) = math.min(path, dist(node.index))
        }
        
        private def visit(): Unit = {
            
            val nexts = new mutable.ListBuffer[(Int, GraphNode)]
            
            nexts += 0 -> root
            while (!nexts.isEmpty) {
                val (path, node) = nexts.remove(0)
                
                if (!visited(node.id)) {
                    visited += node.id
                    set(node, path)
                    
                    nexts ++= node.links.map {l => (path + 1, l)}
                }                
            }            
        }

    }
    
    case class GraphNode(id: Int, links: mutable.Buffer[GraphNode] = new mutable.ArrayBuffer[GraphNode]) {
        val index = id - 1
    }
        
    def readInput(): Seq[Graph] = {
        val sc = new java.util.Scanner(System.in)
        val q = sc.nextInt()
        val graphs = new Array[Graph](q)
        for (i <- 1 to q) {
            val n = sc.nextInt()
            val m = sc.nextInt()
            val nodes = 1 to n map { j => j -> GraphNode(j) } toMap
            
            for (k <- 1 to m) {
                val (from, to) = sc.nextInt() -> sc.nextInt()
                val fromNode = nodes(from)
                val toNode = nodes(to)
                fromNode.links += toNode
                toNode.links += fromNode
            }
            val root = sc.nextInt()
            val rootNode = nodes(root)
            
            graphs(i - 1) = Graph(nodeCount = n, edgeCount = m, root = rootNode)
        }
        graphs.toSeq
    }
    
    def main(args: Array[String]) {
        val graphs = readInput()
        graphs.foreach(g => println(g.distances.mkString(" ")))
    }
}