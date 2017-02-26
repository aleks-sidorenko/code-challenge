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
    
    def ways(num: Int, coins: List[Int]): Long = {
        val cache = new mutable.HashMap[String, Long]()
        
        def key(n: Int, cs: List[Int]): String = s"${n}_${cs.mkString}"
            
        def calc(n: Int, cs: List[Int]): Long = {
            if (n == 0) return 1
            if (n < 0) return 0            
            cache.getOrElseUpdate(key(n, cs), cs.map(c => calc(n - c, cs.filter(_ >= c))).sum)            
        }
        
        calc(num, coins)
     }
    
    def main(args: Array[String]) {
        val (num, coins) = readInput()
        println(ways(num, coins.sorted))
        
    }
}
