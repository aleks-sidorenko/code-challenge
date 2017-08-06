object Solution {

  def readInput(): List[Int] = {
    val sc = new java.util.Scanner(System.in)
    val n = sc.nextInt
    val nums = collection.mutable.ListBuffer.empty[Int]
    for (i <- 0 until n) nums += sc.nextInt()
    nums.toList
  }

  
  class MedianStream {
    private val lowHeap = new collection.mutable.PriorityQueue[Int]()
    private val highHeap = new collection.mutable.PriorityQueue[Int]()(implicitly[Ordering[Int]].reverse)

    def += (num: Int): this.type = {
      if (lowHeap.isEmpty) lowHeap += num
      else {
        val lowMax = lowHeap.head
        if (num < lowMax) lowHeap += num
        else highHeap += num
      }

      rebalance()

      this
    }    

    def median(): Int = if (diff == 0) (getOrDefault(shorter) + getOrDefault(longer)) / 2 else getOrDefault(longer)

    private def getOrDefault(queue: collection.mutable.PriorityQueue[Int]): Int = if (queue.size > 0) queue.head else 0

    private def shorter = if (lowHeap.size <= highHeap.size) lowHeap else highHeap
    private def longer = if (lowHeap.size > highHeap.size) lowHeap else highHeap

    private def diff = longer.size - shorter.size

    private def rebalance(): Unit = {
      if (diff <= 1) return
      val elements = diff / 2
      for (_ <- 1 to elements) shorter.enqueue(longer.dequeue)
      ()
    }
  }

  def main(args: Array[String]) {
    val numbers = readInput()

    val stream = new MedianStream()
    numbers.foreach {n => println((stream += n).median)}
  }
}
