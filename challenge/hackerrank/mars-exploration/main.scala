object Solution {

    def readInput() = {
        val sc = new java.util.Scanner (System.in)
        sc.next()    
    }
    
    def changedLetters(s: String) = {
        val word = "SOS"
        val n = s.length / word.length
        val expected = word * n
        s.zip(expected).map { case (f, s) => if (f != s) 1 else 0 } 
            .sum
    }
    
    
    def main(args: Array[String]) {
        val s = readInput()
        println(changedLetters(s))
    }
}
