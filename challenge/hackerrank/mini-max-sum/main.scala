import scala.collection._
    
object Solution {
    def readInput(): List[Long] = {
        val sc = new java.util.Scanner(System.in)
        val n = 5
        val array = new Array[Long](n)
        for (i <- 0 until n) {
            val el = sc.nextInt()
            array(i) = el
        }
        sc.close()
        array.toList
    }

    def minMax(numbers: List[Long]): (Long, Long) = {
        val sum = numbers.sum
        val min = numbers.min
        val max = numbers.max
        (sum - max, sum - min)
    }

    def main(args: Array[String]) {
        val input = readInput()
        val (min, max) = minMax(input)

        println(s"$min $max")
    }
}