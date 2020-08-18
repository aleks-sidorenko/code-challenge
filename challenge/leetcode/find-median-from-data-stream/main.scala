class MedianFinder() {

    private val low = new collection.mutable.PriorityQueue[Int]()
    private val high = new collection.mutable.PriorityQueue[Int]()(Ordering[Int].reverse)

    private def longest = if (low.size >= high.size) low else high
    private def shortest = if (low.size < high.size) low else high
    
    
    private def rebalance(): Unit = {
        if (longest.size - shortest.length > 1) {
            shortest.enqueue(longest.dequeue)
        }
    }
    
    def addNum(num: Int) {
       if (low.headOption.getOrElse(-1) > num) low.enqueue(num) else high.enqueue(num)
       rebalance()
    }

    def findMedian(): Double = {
        if (longest.size == shortest.size) (shortest.head + longest.head).toDouble / 2
        else longest.head.toDouble
    }


}
