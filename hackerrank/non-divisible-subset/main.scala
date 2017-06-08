object Solution {
    
    def readInput(): (List[Int], Int) = {
        val sc = new java.util.Scanner(System.in)
        val (n, k) = (sc.nextInt, sc.nextInt)
        val set = new Array[Int](n)
        for (i <- 0 until n) {
            set(i) = sc.nextInt
        }

        (set.toList, k)
        
    }

    def nonDivisibleSubset(numbers: List[Int], k: Int): Int = {
        def ok(max: List[Int], x: Int) = max.forall(m => (m + x) % k != 0)

        def loop(numbers: List[Int], max: List[Int]): Int = numbers match {
            case x :: xs => 
                math.max(
                    if (ok(max, x)) loop(xs, x :: max) else max.length,
                    loop(xs, max)
                )
            case Nil => max.length
        }
        
        loop(numbers, Nil)
    }

    def main(args: Array[String]) {
        val (numbers, k) = readInput()

        println(nonDivisibleSubset(numbers, k))
    }
}