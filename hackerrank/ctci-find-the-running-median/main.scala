import scala.collection._
    
object Solution {
    def readInput(): List[Int] = {
        val sc = new java.util.Scanner (System.in);
        var n = sc.nextInt();
        var a = new Array[Int](n);
        for(a_i <- 0 to n-1) {
           a(a_i) = sc.nextInt();
        }
        a.toList
    }
        

    object Median {
        private val heapMax = new mutable.PriorityQueue[Int]()
        private val heapMin = new mutable.PriorityQueue[Int]()(implicitly[Ordering[Int]].reverse)
                
        def median(num: Int): Double  = {
            add(num)
            calc()
        }
        
        private def add(num: Int) = {
            
            val max = if (!heapMax.isEmpty) heapMax.head else Int.MinValue
            val min = if (!heapMin.isEmpty) heapMin.head else Int.MaxValue
            
            if (num <= max) {
                heapMax.enqueue(num)
            } else {
                heapMin.enqueue(num)
            }
                            
            rebalance()
        }
        
        private def longest = if (heapMax.length >= heapMin.length) heapMax else heapMin
        private def shortest = if (heapMax.length >= heapMin.length) heapMin else heapMax
            
        private def rebalance() = {
            
            val long = longest
            val short = shortest
            
            while (long.length - short.length > 1) {
                val v = long.dequeue
                short.enqueue(v)
            }
            
        }
        
        private def calc(): Double = {
            val long = longest
            val short = shortest
            
            if (long.length == short.length) {
                (long.head + short.head).toDouble / 2
            } else {
                long.head.toDouble
            }
        }
    }
        
    def main(args: Array[String]) {
        val input = readInput()
        
        input.foreach { i =>             
            println(f"${Median.median(i)}%1.1f") }
    }
}
