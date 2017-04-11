object Solution {

    def readInput(): List[Int] = {
        val sc = new java.util.Scanner (System.in)
        var n = sc.nextInt()
        var score = new Array[Int](n)
        for(score_i <- 0 to n-1) {
           score(score_i) = sc.nextInt()
        }
        score.toList
    }
    
    def getBreakings(score: List[Int]): (Int, Int) = {
        val (most, _, least, _) = score.foldLeft((0, score.head, 0, score.head))( 
                                                 { (res, s) => 
                                                    var (most, mostScore, least, leastScore) = res
                                                    if (s > mostScore) {
                                                        mostScore = s
                                                        most += 1
                                                    }
                                                    if (s < leastScore) {
                                                        leastScore = s
                                                        least += 1
                                                    }
                                                    (most, mostScore, least, leastScore)
                                                 }
                                                )
        (most, least)
    }
    
    def main(args: Array[String]) {
        val score = readInput()
        val breakings = getBreakings(score)
        println(s"${breakings._1} ${breakings._2}")
    }
}
