import scala.collection._
    
object Solution {

    def readInput() : (Int, List[(Int, Int)]) = {
        val sc = new java.util.Scanner (System.in)
        var N = sc.nextInt()
        assert (N >= 1)
            
        var I = sc.nextInt()            
        assert (I >= 1)
        val pairs = new mutable.ArrayBuffer[(Int, Int)](I)
            
        for(i <- 0 to I-1) {
           val p = (sc.nextInt(), sc.nextInt())
           pairs += p
        }
        
        (N, pairs.toList)
    }
    
    def main(args: Array[String]) {
        val (n, pairs) = readInput()
        println(n)
            println(pairs)
        
    }
}