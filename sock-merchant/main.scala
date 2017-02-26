object Solution {

    def readInput(): List[Int] = {
        val sc = new java.util.Scanner(System.in)
        val n = sc.nextInt()
        val socks = new Array[Int](n)
        for (i <- 0 until n) {
            socks(i) = sc.nextInt()
        }
        socks.toList
    }
    
    def calcPairs(socks: List[Int]): Int = {
        socks.groupBy(identity).map { case (s, ss) => ss.length / 2 }
            .sum
    }
    
    def main(args: Array[String]) {
        val socks = readInput()
        println(calcPairs(socks))    
    }
}
