import scala.collection._
    
object Solution {
    sealed trait Action {}
    case class Query(i: Int) extends Action
    case class Merge(i: Int, j: Int) extends Action
    
       
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
    
    def applyAction(communities: mutable.Map[Int, mutable.Set[Int]], action: Action) = {
        action match {
            case Query(i) => println(communities(i).size)
            case Merge(i, j) => {
                val comI = communities(i)
                val comJ = communities(j)
                val merged = comI.union(comJ)
                merged.foreach(k => communities(k) = merged)
            }
        }
    }
    
    def processActions(communities: mutable.Map[Int, mutable.Set[Int]], actions: List[Action]) = {
        actions.foreach(applyAction(communities, _))
    }
    
    def main(args: Array[String]) {
        val (n, actions) = readInput()
        val communities = (1 to n).groupBy(identity).mapValues(i => mutable.Set(i.head))
        processActions(mutable.Map() ++ communities, actions)
    }
}