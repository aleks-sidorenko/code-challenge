object Solution {
    def longestCommonSubsequence(text1: String, text2: String): Int = {
        val cache = Array.ofDim[Int](text1.length, text2.length)
        val cached: (Int, Int) => Int = (i, j) => if (i >= 0 && i < text1.length && j >= 0 && j < text2.length) cache(i)(j) else 0
        
        for {
            i <- 0 until text1.length
            j <- 0 until text2.length
        } {
            cache(i)(j) = if (text1(i) == text2(j)) cached(i - 1, j - 1) + 1
            else math.max(cached(i, j - 1), cached(i - 1, j))
        }
        
        cached(text1.length - 1, text2.length - 1)
    }
}