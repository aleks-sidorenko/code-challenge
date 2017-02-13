object Solution {
    def readInput(): (Int, Array[Int]) = {
        val sc = new java.util.Scanner (System.in);
        var n = sc.nextInt();
        var k = sc.nextInt();
        var a = new Array[Int](n);
        for(a_i <- 0 to n-1) {
           a(a_i) = sc.nextInt();
        }
        sc.close
        (k, a)
    }
    
    def rotateLeft(array: Array[Int], k: Int): Array[Int] = {
        val shifted = array.take(k)
        val rest = array.drop(k)
        rest ++ shifted
    }

    def main(args: Array[String]) = {
        val (k, array) = readInput()
        print(rotateLeft(array, k).mkString(" "))
        
    }
}
