object Solution {
    def multiply(num1: String, num2: String): String = {
        def strToList(n: String): List[Byte] = n.toList.map(x => (x -'0').toByte)
        def listToStr(n: List[Byte]): String = n.map('0' + _).mkString
        
        def add(n1: List[Byte], n2: List[Byte]): List[Byte] = ???
        def mult(n1: List[Byte], n2: Byte, dig: Int = 0): List[Byte] = ???
        
        val n1: List[Byte] = strToList(num1)
        val n2: List[Byte] = strToList(num2)
        
        val (res, _) = n1.foldRight((n2, 0)) { case (n, (acc, d)) => 
            add(acc, mult(acc, n, d)) -> (d + 1)
        }
        
        listToStr(res)
    }
}