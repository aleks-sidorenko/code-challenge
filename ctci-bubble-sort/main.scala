object Solution {
    
        
    @inline
    def swap(array: Array[Int], i: Int, j: Int) : Unit = {
        val tmp = array(i)
        array(i) = array(j)
        array(j) = tmp
    }
    
    def bubbleSort(array: Array[Int]): Int = {
        var totalSwaps = 0
        for (i <- 0 to array.length - 1) {
            var swaps = 0
            for (j <- 0 to array.length - 2 ) {
                if (array(j) > array(j + 1)) {
                    swap(array, j, j + 1)
                    swaps += 1
                }    
            }
            totalSwaps += swaps
        }
        
        totalSwaps
    }
        
    def main(args: Array[String]) {
        val sc = new java.util.Scanner (System.in)
        var n = sc.nextInt()
        assert (n >= 2)
        var a = new Array[Int](n)
        for(a_i <- 0 to n-1) {
           a(a_i) = sc.nextInt()
        }
            
        val result = bubbleSort(a)
        println(s"Array is sorted in ${result} swaps.")
        val (first, last) = (a(0), a(a.length - 1))
        println(s"First Element: ${first}")
        println(s"Last Element: ${last}")
    }
}
