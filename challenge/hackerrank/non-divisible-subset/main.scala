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
        val deduction = numbers.map(_ % k)
            .groupBy(identity).mapValues { _.length }

        def lengthFor(d: Int): (Int, Int) = {
            val v = deduction.getOrElse(d, 0)
            val o = deduction.getOrElse(k - d, 0)
            if (d == 0 || d * 2 == k) d -> math.min(1, v)
            else if (v >= o) d -> v
            else (k - d) -> o
        }
        
        def loop(i: Int, included: Set[Int], length: Int): Int = {
            if (i == k) length
            else {
                val (d, l) = lengthFor(i)
                if (included(d)) loop(i + 1, included, length)
                else loop(i + 1, included + d, length + l)
            }
        }

        loop(0, Set.empty[Int], 0)
    }


    def main(args: Array[String]) {
        val (numbers, k) = readInput()

        println(nonDivisibleSubset(numbers, k))
    }
}