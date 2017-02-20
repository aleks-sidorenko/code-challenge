object Solution {
    
    case class Trip(m: Int, flavors: List[Int])
        
    def readInput(): List[Trip] = {
        val sc = new java.util.Scanner (System.in)
        val t = sc.nextInt()
        val trips = new Array[Trip](t)
        var a0 = 0
        while(a0 < t){
            var m = sc.nextInt()
            var n = sc.nextInt()
            var a = new Array[Int](n)
            for(a_i <- 0 to n-1) {
               a(a_i) = sc.nextInt()
            }
            trips(a0) = Trip(m, a.toList)
            a0+=1;
        }
        trips.toList
    }

    def main(args: Array[String]) {
        val trips = readInput()
        println(trips.mkString("\n"))
       
    }
}
