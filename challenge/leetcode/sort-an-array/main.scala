object Solution {
        
    def longestPalindrome(s: String): String = {
        
        val chars = s.toArray
        val cache = Array.ofDim[Boolean](s.length, s.length)
                
        @inline
        def isPalindrom(from: Int, to: Int): Boolean = {
            cache(from)(to) = chars(from) == chars(to) && (to - from < 3 || cache(from + 1)(to - 1)) 
            cache(from)(to)
        } 
        
        var from = 0
        var to = -1
        
        for {
            f <- s.length - 1 to 0 by -1
            t <- f until s.length
        } { 
            if (isPalindrom(f, t) && to - from < t - f) {
                to = t
                from = f
            }
        }
                        
        s.substring(from, to + 1)
    }
}