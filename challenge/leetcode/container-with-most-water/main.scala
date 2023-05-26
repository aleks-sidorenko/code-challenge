object Solution {
    def maxArea(height: Array[Int]): Int = {
        def min(a: Int, b: Int): Int = if (a < b) a else b 
        def max(a: Int, b: Int): Int = if (a > b) a else b
        def water(from: Int, to: Int) = min(height(from), height(to))* (to - from)        
        
        var (l, r)  = (0, height.length - 1)
        var ret = 0

        while (l < r) {
            val nexts = (l, r) :: (l + 1, r) :: (l, r - 1) :: (l + 1, r - 1) :: Nil
            ret = max(ret, nexts.map { case (i, j) => water(i, j) }.max)
            if (height(l) < height(r)) l = l + 1
            else r = r - 1
        }
        ret
        
    }
}