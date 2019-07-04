object Solution {
    def maxProfit(prices: Array[Int]): Int = {
        def profit(a: Int, b: Int): Int = if (b - a > 0) b - a else 0
        
        def loop(ps: List[Int], max: Int, buy: Int): Int = {
            ps match {
                case p :: px => 
                    loop(px, math.max(max, p - buy), math.min(buy, p))
                case _ => max
            }
        }
        
        if (prices.nonEmpty) loop(prices.toList, 0, prices.head)
        else 0
        
    }
}