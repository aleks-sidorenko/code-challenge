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
    
    def processActions(communities: Map[Int, Set[Int]], actions: List[Action]) = {
        println(communities)
    }
    
    def main(args: Array[String]) {
        val (n, actions) = readInput()
        val communities = (1 to n).groupBy(identity).mapValues(i => Set(i.head))
        processActions(communities, actions)
    }
}