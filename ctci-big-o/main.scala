import scala.collection._
object Solution {

    object Prime {
        private val primes = new mutable.HashSet[Int]()    
        primes ++= List(2, 3, 5, 7, 11)
        private var last = primes.max
            
        private def add(num: Int) = {
            last = num
            primes += num
        }
        
        private def isNotPrime(num: Int) = num == 1 || primes.exists(p => num % p == 0)
    }
    
    implicit class Prime(val number: Int) extends AnyVal {
        
        import Prime._
                    
        def isPrime(): Boolean = {
            if (primes(number)) return true
            if (isNotPrime(number)) return false
            val limit = math.floor(math.sqrt(number)).toInt
                            
            val start = last
            
            for (i <- start to limit by 2) {
                if (!isNotPrime(i)) {
                    add(i)
                    if (number % i == 0) return false
                }
            }
            true
        }
    }
    
    def readInput(): List[Int] = {
        val sc = new java.util.Scanner (System.in)
        var p = sc.nextInt()
        
        var numbers = new Array[Int](p)
        for (i <- 0 to p - 1) numbers(i) = sc.nextInt()
        numbers.toList
    }
    
    def main(args: Array[String]) {
        val numbers = readInput()
        numbers.map(n => if (n.isPrime) "Prime" else "Not prime").foreach(println(_))
    }
}
