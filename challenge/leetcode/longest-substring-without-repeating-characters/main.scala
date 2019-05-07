object Solution {
    import scala.collection.mutable.{Map, LinkedHashMap}
    
    def lengthOfLongestSubstring(s: String): Int = {
        
        def loop(chars: List[(Char, Int)], cur: Int, max: Int, seen: Map[Char, Int]): Int = {
            chars match {
                case (x, i) :: xs if seen.contains(x) => loop(xs, i - seen(x), max, seen.takeRight(i - seen(x) - 1) + (x -> i) )
                case (x, i) :: xs => loop(xs, cur + 1, math.max(cur + 1, max), seen + (x -> i))
                case _ => max
            }
        }
        
        loop(s.toList.zipWithIndex, 0, 0, new LinkedHashMap[Char, Int]())
        
    }
}