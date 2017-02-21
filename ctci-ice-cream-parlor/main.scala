object Solution {
    
    
        
    case class Trip(m: Int, flavors: IndexedSeq[Int]) {
        
        lazy val optimized = flavors.zipWithIndex.filter(_._1 < m)
                    
        def matched(): (Int, Int) = {
            
            val valid = optimized.groupBy(_._1).map { case(k, v) => (k, v.map(_._2))}
            val discounted = optimized.map(m - _._1).filter(v => m - v != v).toSet
            val discounted2 = optimized.map(m - _._1).filter(v => m - v == v).toSet
            val intersection = valid.keySet & discounted
            val intersection2 = valid.keySet & discounted2
                
            def indexes = (set: Set[Int]) => set.flatMap(valid(_)).map(_ + 1).toList.sorted
            val inter = if (intersection.isEmpty) intersection2 else intersection
            val ret = indexes(inter)
                        
            (ret(0), ret(1))
            
        }
                
    }
        
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
            trips(a0) = Trip(m, a)
            a0+=1
        }
        trips.toList
    }

    def main(args: Array[String]) {
        val trips = readInput()
        trips.map(_.matched).map { case (f, s) => s"$f $s" } foreach(println(_))
       
    }
}
