object Solution {
    def deckRevealedIncreasing(deck: Array[Int]): Array[Int] = {
        
        def shift(lst: Seq[Int]): Seq[Int] = {
            if (lst.isEmpty || lst.size == 1) lst
            else {
                val (head, last) = (lst.take(lst.length - 1), lst.last)
                last +: head
            }
        }
        val sorted = deck.toList.sorted.reverse
        sorted.foldLeft(Seq.empty[Int]) { (acc, x) => 
            x +: shift(acc)
        }.toArray
    }
}