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
    
    
    
    def rotateLeft(array: Array[Int], start: Int, finish: Int): Unit = {
        val middle = (finish + start) / 2
        for (i <- start to middle) {
            val tmp = array(i)    
            array(i) = array(start + finish - i)
            array(start + finish - i) = tmp
        }
        
    }

    def main(args: Array[String]) = {
        val (k, array) = readInput()
        val n = array.length
        rotateLeft(array, 0, n - 1)        
        rotateLeft(array, 0, n - k - 1)
        rotateLeft(array, n - k, n - 1)
        
        
        print(array.mkString(" "))
        
    }
}
