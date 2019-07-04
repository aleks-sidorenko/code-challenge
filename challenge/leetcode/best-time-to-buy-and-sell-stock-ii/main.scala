object Solution {
    def maxProfit(prices: Array[Int]): Int = {
        def profit(a: Int, b: Int): Int = if (b - a > 0) b - a else 0
        
        def loop(ps: List[Int], profit: Int, buy: Int, sell: Int): Int = {
            ps match {
                case p :: px => 
                    if (p < sell) loop(px, profit + sell - buy, p, p)
                    else loop(px, profit, buy, math.max(sell, p))
                case _ => profit + sell - buy
            }
        }
        
        if (prices.nonEmpty) loop(prices.toList, 0, prices.head, prices.head)
        else 0
        
    }
}