object Solution {

    def readInput(): List[Int] = {
        val sc = new java.util.Scanner (System.in);
        val s = sc.nextInt();
        val stairs = new Array[Int](s)
        for (i <- 0 until s) {
            stairs(i) = sc.nextInt()
        }
        stairs.toList
    }
    
    val steps = List(1, 2, 3)
    
    def ways(num: Int): Int = {
        num match {
            case n if n < 0 => 0
            case n if n == 0 => 1
            case _ => steps.map(s => ways(num - s)).sum
        }
    }
    
    def main(args: Array[String]) {
        val stairs = readInput()
        stairs.map(s => ways(s)).foreach(println(_))
        
    }
}
