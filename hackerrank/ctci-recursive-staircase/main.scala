import scala.collection._
    
object Solution {

    def readInput(): List[Int] = {
        val sc = new java.util.Scanner (System.in);
        val s = sc.nextInt();
        val stairs = new Array[Int](s)
        for (i <- 0 until s) {
            stairs(i) = sc.nextInt()
        }
        stairs.toList
    }
    
    val steps = List(1, 2, 3)
    val cache = new mutable.HashMap[Int, Int]()
        
    def ways(num: Int): Int = {
        if (num == 0) return 1
        if (num < 0) return 0
        cache.getOrElseUpdate(num, steps.map(s => ways(num - s)).sum)
     }
    
    def main(args: Array[String]) {
        val stairs = readInput()
        stairs.map(s => ways(s)).foreach(println(_))
        
    }
}
