import scala.collection._
    
object Solution {

    def readInput(): List[Int] = {
        val sc = new java.util.Scanner (System.in)
        val n = sc.nextInt()
                    
        val numbers = new Array[Int](n)
        for (i <- 0 until n) {
            numbers(i) = sc.nextInt()
        }
        
        numbers.toList
    }
    
    def unique(numbers: List[Int]): Int = {
        numbers.groupBy(identity).map { case (n, ns) => n -> ns.length }
            .filter { case (n, l) => l % 2 != 0 }
            .map { case (n, l) => n }
            .head
    }
        
    def main(args: Array[String]) {
        val numbers = readInput()
        
        println(unique(numbers))
        
    }
}