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

    def inversions(numbers: List[Int]): Long = {

        def merge(array: Array[Int], start: Int, middle: Int, end: Int): Long = {

            val left = array.slice(start, middle)
            val right = array.slice(middle, end)

            var i, j = 0
            var inv: Long = 0
            var k = start

            while (i < left.length && j < right.length) {
                if (left(i) <= right(j)) {
                    array(k) = left(i)
                    i += 1
                }
                else {
                    array(k) = right(j)
                    j += 1
                    inv += (left.length - i)
                }
                k += 1
            }
            
            while (i < left.length) {
                array(k) = left(i)
                i += 1
                k += 1
            }

            while (j < right.length) {
                array(k) = right(j)
                j += 1
                k += 1
            }

            inv
        }

        def mergeSort(array: Array[Int], start: Int, end: Int): Long = {
            if (start + 1 >= end) return 0
            val middle: Int = (end + start) / 2
            mergeSort(array, start, middle) + mergeSort(array, middle, end) + merge(array, start, middle, end)
        }
        
        mergeSort(numbers.toArray, 0, numbers.length)
    }

    def main(args: Array[String]) {
        val cases = readInput()
        cases.foreach { c => println(inversions(c)) }
    }
}