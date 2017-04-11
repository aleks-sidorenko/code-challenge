import scala.collection._
    
object Solution {
    sealed trait Action {}
    case class Query(i: Int) extends Action
    case class Merge(i: Int, j: Int) extends Action
    

    class Communities(n: Int) {
        val elements: Array[Int] = (0 until n).toArray
        
        val sizes: Array[Int] = Array.fill(n)(1)

        @inline
        private def toIndex(i: Int) = i - 1

        def apply(a: Int) = query(a)

        def union(a: Int, b: Int): Unit = {
            val rootA = findRoot(a)
            val rootB = findRoot(b)

            if (rootA == rootB) return

            val (min, max) = (math.min(rootA, rootB), math.max(rootA, rootB))

             elements(max) = min
             sizes(min) += sizes(max)
        }

        def query(a: Int): Int = {
            sizes(findRoot(a))
        }
        
        private def findRoot(a: Int) = {
            var i = toIndex(a)
            while (i != elements(i)) i = elements(i)
            i
        }
    }

    def readInput(): (Int, List[Action])  = {
        val sc = new java.util.Scanner (System.in)
        val (n, q) = (sc.nextInt(), sc.nextInt())
        
            val queries = new Array[Action](q)
            
        for (i <- 0 until q) {
            val a = sc.next()
            
            val action = a match {
                case "Q" => Query(sc.nextInt())
                case "M" => Merge(sc.nextInt(), sc.nextInt())
                case _ => throw new IllegalArgumentException()
            }
            queries(i) = action
        }
        
        (n, queries.toList)
        
    }    
    
    def applyAction(communities: Communities, action: Action) = {
        action match {
            case Query(i) => println(communities(i))
            case Merge(i, j) => communities.union(i, j)
        }
    }
    
    def processActions(communities: Communities, actions: List[Action]) = {
        actions.foreach(applyAction(communities, _))
    }
    
    def main(args: Array[String]) {
        val (n, actions) = readInput()
        val communities = new Communities(n)

        processActions(communities, actions)
    }
}