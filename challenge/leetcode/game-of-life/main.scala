object Solution {
    def gameOfLife(board: Array[Array[Int]]): Unit = {
      
        type Coord = (Int, Int)

        val (m, n): Coord = (board.length, board.headOption.map(_.length).getOrElse(0))
        
        val check: Coord => Boolean = ((i: Int, j: Int) => i >= 0 && i < m && j >= 0 && j < n).tupled
        
        val neighbors: Coord => List[Coord] = ((i: Int, j: Int) => ((i - 1, j - 1) :: (i - 1, j) :: (i - 1, j + 1) :: (i, j - 1) :: (i, j + 1) :: (i + 1, j - 1) :: (i + 1, j) :: (i + 1, j + 1) :: Nil).filter(check(_))).tupled
        
        val original: Seq[Seq[Int]] = board.map(_.toVector).toVector

        def alive(c: Coord): Boolean = {
            val (i, j) = c
            val cur: Boolean = original(i)(j) > 0
            
            val alives = neighbors(i -> j).map { case (k, l) => original(k)(l) }.sum
            
            (cur, alives) match {
                case (true, 2) | (true, 3) => true
                case (false, 3)    => true
                case (true, x) if (x < 2 || x > 3) => false
                case _ => cur
            }            
        }
        
        for {
            i <- 0 until m
            j <- 0 until n            
        } {            
            board(i)(j) = if (alive(i -> j)) 1 else 0
        }
        
    }
}