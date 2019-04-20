object Solution {
    def maxIncreaseKeepingSkyline(grid: Array[Array[Int]]): Int = {
        val n = grid.length
        
        val left = (0 until n).map(i => grid(i).max)
        val top = (0 until n).map(j => (0 until n).map(i => grid(i)(j)).max)
        println(left)
        
        (for {
            i <- 0 until n
            j <- 0 until n   
        } yield math.min(left(i), top(j)) - grid(i)(j)).sum
             
    }
}