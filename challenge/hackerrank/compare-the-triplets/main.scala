object Solution {

    type Tripple = (Int, Int, Int)
    
    implicit class TrippleOps(value: Tripple) {
        def score(other: Tripple): Int = {
           val res = (value._1 - other._1) :: (value._2 - other._2) :: (value._3 - other._3) :: Nil
           res.filter(_ > 0).length
        }
    }
        
    def readInput() : (Tripple, Tripple) = {
        val sc = new java.util.Scanner (System.in)
        var a0 = sc.nextInt()
        var a1 = sc.nextInt()
        var a2 = sc.nextInt()
        var b0 = sc.nextInt()
        var b1 = sc.nextInt()
        var b2 = sc.nextInt()
            
        ((a0, a1, a2), (b0, b1, b2))
        
    }
    def main(args: Array[String]) {
        val (a, b) = readInput()
        println(s"${a.score(b)} ${b.score(a)}")
    }
}
