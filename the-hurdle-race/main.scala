object Solution {

    def readInput(): (Int, List[Int]) = {
        val sc = new java.util.Scanner (System.in)
        var n = sc.nextInt()
        var k = sc.nextInt()
        var height = new Array[Int](n)
        for(height_i <- 0 to n-1) {
           height(height_i) = sc.nextInt()
        }
        (k, height.toList)
    }
    
    def getMinBeverages(k: Int, heights: List[Int]): Int = {
        val max = heights.max
        if (k < max) max - k else 0
    }
    
    def main(args: Array[String]) {
        val (k, heights) = readInput()
         println(getMinBeverages(k, heights))
    }
}
