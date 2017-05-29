object Solution {
    final val max = 100
    
    def readInput(): List[List[Int]] = {
        val sc = new java.util.Scanner(System.in)    
        val d = sc.nextInt
        val cases = new Array[Array[Int]](d)
        for (i <- 0 until d) {
            val n = sc.nextInt
            val c = new Array[Int](n)
            for (j <- 0 until n) {
                c(j) = sc.nextInt
            }
            cases(i) = c
        }
        cases.map(_.toList).toList
        
    }

    def inversions(numbers: List[Int]): Int = {
        numbers.head
    }

    def main(args: Array[String]) {
        val cases = readInput()
        cases.foreach { c => println(inversions(c)) }
    }
}