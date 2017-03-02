object Solution {

    def readInput(): (Int, List[(Int, Int)]) = {
        val sc = new java.util.Scanner (System.in)
        var m = sc.nextInt()
        var n = sc.nextInt()
        var i = 0
        val moves = new Array[(Int, Int)](n)
            
        while(i < n) {
            var a = sc.nextInt()
            var b = sc.nextInt()
            moves(i) = (a, b)
            i +=1
        }
        
        (m, moves.toList)
        
    }
    
    def main(args: Array[String]) {
        val (m, moves) = readInput()
        val res = moves.foldLeft(m) { (marble, move) => 
            marble match {
                case move._1 => move._2
                case move._2 => move._1
                case _ => marble
                
            }
        }
        println(res)
    }
        
    
}
