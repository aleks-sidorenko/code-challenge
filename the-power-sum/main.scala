object Solution {
    
    def numberOfWays(x: Int, numbers: List[Int]): Int = {        
        if (x == 0) 1
        else numbers match {
          case head::tail => numberOfWays(x - head, tail) + numberOfWays(x, tail)
          case Nil => 0
        } 
    }
    
    @inline
    def pow(x: Int, n: Int) = {
        var acc = 1
        for (_ <- 1 to n) acc *= x
        acc
    }
    
    def numberOfWays(x: Int, n: Int): Int = {
       val numbers = Stream.from(1).map(i => pow(i, n)).takeWhile( _ <= x ).toList
       
       numberOfWays(x, numbers)
    }

    def main(args: Array[String]) {
       println(numberOfWays(readInt(),readInt()))
    }
}
