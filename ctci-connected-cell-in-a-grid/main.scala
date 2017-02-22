import scala.collection._
    
object Solution {

    case class Cell(value: Int) extends AnyVal {
        def isFilled() = value == 1
    }
    
    type Location = (Int, Int)
    type Region = mutable.HashSet[Location]
    
    implicit class LocationOps(val location: Location) {
        val i = location._1
        val j = location._2
        def isValid(n: Int, m: Int) = i >= 0 && i < n && j >= 0 && j < m
        def neighbors(n: Int, m: Int): List[Location] = {            
            val all = (i - 1, j - 1) :: (i - 1, j) :: (i - 1, j + 1) :: (i, j + 1) :: (i + 1, j + 1)  :: (i + 1, j) :: (i + 1, j - 1) ::  (i, j - 1) :: Nil
            all.filter(_.isValid(n, m))                
        }
    }
            
    case class Grid(n: Int, m: Int, matrix: Array[Array[Cell]]) {
        
        private val regions = new mutable.ListBuffer[Region]()
        
        private final val ZeroLocation = (0, 0)
        
        def apply(location: Location) = matrix(location.i)(location.j)
            
        def maxRegions(): Int = {
            val locations = new mutable.HashSet[Location]()
            
            def process(location: Location): Unit = {
                if (this(location).isFilled) {
                    
                    getRegion(location) match {
                        case Some(region) => region += location
                        case _ => val region = new Region(); region += location; regions += region
                    }
                }                
            }
            
            def traverse(location: Location): Unit = {
                if (locations(location)) return
                    
                process(location)
                    
                val neighbors = location.neighbors(n, m)
                neighbors.foreach(process(_))
                locations += location
                
                neighbors.foreach(traverse(_))
                
            }
            
            traverse(ZeroLocation)            
            
            if (regions.isEmpty) 0 else regions.maxBy(_.size).size
            
        }
        
        
        private def getRegion(location: Location): Option[Region] = {
            val neighbors = location.neighbors(n, m).filter(n => matrix(n._1)(n._2).isFilled)
            regions.find(r => neighbors.exists(n => r(n)))
        }
        
        def print() = {
            matrix.foreach(r => println(r.mkString(" ")))
            regions.foreach(r => println(r))
        }        
        
    }
                                                  
    def readInput(): Grid = {
        val sc = new java.util.Scanner (System.in)
        var n = sc.nextInt()
        var m = sc.nextInt()
        var grid = Array.ofDim[Cell](n,m)
        for(grid_i <- 0 to n-1) {
           for(grid_j <- 0 to m-1){
              grid(grid_i)(grid_j) = Cell(sc.nextInt())
           }
        }
        Grid(n, m, grid)
    }
                                                  
    def main(args: Array[String]) {
        val grid = readInput()
        println(grid.maxRegions)
    }
}
