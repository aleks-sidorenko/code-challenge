object Solution {
    final val max = 100
    
    def readInput(): List[Int] = {
        val sc = new java.util.Scanner(System.in)    
        val n = sc.nextInt
        val array = new Array[Int](n)
        for (i <- 0 until n) {
            array(i) = sc.nextInt
        }
        array.toList
        
    }

    def pick(numbers: List[Int]) = {
        val ranges = Array.fill(max)(0)
        @inline def toIndex(n: Int) = n - 1

        numbers.foreach { n => 
            val index = toIndex(n)
            val indexes = (index - 1 :: index :: Nil).filter(i => i >= 0 && i < max - 1)
            indexes.foreach(i => ranges(i) += 1)
        }
        ranges.max
    }
    
    def main(args: Array[String]) {
        val numbers = readInput()
        println(pick(numbers))
    }
}