import scala.collection._
    
object Solution {

    def readInput(): (Int, List[Int]) = {
        val sc = new java.util.Scanner (System.in)
        val n = sc.nextInt()
        val coinsCount = sc.nextInt()
            
        val coins = new Array[Int](coinsCount)
        for (i <- 0 until coinsCount) {
            coins(i) = sc.nextInt()
        }
        (n, coins.toList)
    }
    
    
        
    def ways(num: Int, coins: List[Int]): Int = {        
        val cache = new mutable.HashMap[Int, Int]()                
        def calc(n: Int, min: Int): Int = {
            println(s"$n, $min")
            println(cache.mkString(" "))
            if (n == min) return cache.getOrElseUpdate(n, 1)
            if (n < min) return 0            
            
            val cs = coins.filter(_ >= min)
            
            cache.getOrElseUpdate(n, cs.map(c => calc(n - c, c)).sum)
        }
        
        calc(num, 0)
     }
    
    def main(args: Array[String]) {
        val (num, coins) = readInput()
        println(ways(num, coins.sorted))
        
    }
}
