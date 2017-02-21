object Solution {
    
    
        
    case class Trip(m: Int, flavors: IndexedSeq[Int]) {
        // bit string which define match like '001100'
        type Bits = String
        
        private lazy val optimizedFlavors = flavors.zipWithIndex.filter(_._1 < m).sortBy(_._1)
                    
        def matched(): (Int, Int) = {
            val initial = "0" * optimizedFlavors.length
            
            def matched(total: Int, level: Int, mch: Bits): Option[Bits] = {
                                
                val numBits = bits(mch)
                                                   
                if (total == 0) {
                    if (numBits == 2) return Some(mch)
                    else return None                    
                }
                
                    
                if (level > optimizedFlavors.length || total < 0) {
                    return None
                }
                                
                val i = level - 1
                val cur = optimizedFlavors(i)._1
                val newMch = setBit(mch, i)
                 
                val right = matched(total - cur, level + 1, newMch)
                if (right.isDefined) {
                    return right
                }
                
                val left = matched(total, level + 1, mch)
                if (left.isDefined) {
                    return left
                }
                
                return None
                
            }
            
            val res = matched(m, 1, initial)
            res match {
                case Some(ret) => bitsToTuple(ret)
                case _ => throw new IllegalArgumentException()
            }
        }
        
        private def bitsToTuple(mch: Bits): (Int, Int) = {
            
            val indexes = mch.zipWithIndex.filter { case (b, i) => b == '1' } map { case (_, i) => i }
            
            val ids = indexes.map({ i => optimizedFlavors(i)._2 + 1 }).sorted
            (ids(0), ids(1))
        }
        
        private def bits(mch: Bits): Int = mch.count(_ == '1')
            
        private def setBit(mch: Bits, i: Int): Bits = {
            val str = new StringBuilder(mch)
            str(i) = '1'
            str.toString.asInstanceOf[Bits]
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
            a0+=1;
        }
        trips.toList
    }

    def main(args: Array[String]) {
        val trips = readInput()
        trips.map(_.matched).map { case (f, s) => s"$f $s" } foreach(println(_))
       
    }
}
