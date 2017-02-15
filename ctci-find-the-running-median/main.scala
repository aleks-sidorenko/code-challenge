object Solution {
    def readInput(): List[Int] = {
        val sc = new java.util.Scanner (System.in);
        var n = sc.nextInt();
        var a = new Array[Int](n);
        for(a_i <- 0 to n-1) {
           a(a_i) = sc.nextInt();
        }
        a.toList
    }
    
    def median(num: Int): Double = {
        num.toDouble
    }

    def main(args: Array[String]) {
        val input = readInput()
            input.foreach { i => println(f"${median(i)}%1.1f") }
    }
}
