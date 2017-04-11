object Solution {
    
    final case class Case(passwords: List[String], attempt: String) {
        def check(): List[String] = {
            check(attempt, passwords) match {
                case None => Nil
                case Some(l) => l
            }
        }
        
        private def check(str: String, words: List[String]): Option[List[String]] = {
            if (str.isEmpty) return Some(Nil)
            
            val matched = words.find(str.startsWith(_))
            
            matched match {
                case Some(m) => 
                    val rest = str.slice(m.length, str.length)
                    check(rest, words) match {
                       case Some(list) => Some(m :: list)
                       case None => None
                    }
                case None => None
            }   
        }
    }
    
    def readInput(): List[Case] = {
        val sc = new java.util.Scanner(System.in)    
        val t = sc.nextInt
        val cases = new Array[Case](t)
        for (i <- 0 until t) {
            val n = sc.nextInt
            val passwords = new Array[String](n)
            for (j <- 0 until n) {
                passwords(j) = sc.next()
            }
            val attempt = sc.next()
            cases(i) = Case(passwords.toList, attempt)    
        }
        cases.toList
        
    }
    
    def main(args: Array[String]) {
        val cases = readInput()
        cases.foreach { c => 
            println(c.check match {
                case Nil => "WRONG PASSWORD"
                case l: List[String] => l.mkString(" ")
            })
        }
        
    }
}