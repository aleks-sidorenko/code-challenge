object Solution {

    def readInput() : List[String] = {
        val sc = new java.util.Scanner (System.in)
        var t = sc.nextInt()
        var a0: Int = 0
        val array = new Array[String](t)
        while(a0 < t){
            var expression = sc.next()
            array(a0) = expression
            a0 += 1;
        }
        
        array.toList
        
        
    }
    
    def validate(exp: String): Boolean = {
        val symbols = new collection.mutable.Stack[Char]()
        val dict = Map( '{' -> '}', '(' -> ')', '[' -> ']' )
        
        
            
        for (ch: Char <- exp) {
            if (dict.contains(ch)) {
                symbols.push(ch)
                
            } else {
                if (ch == dict(symbols.top)) {
                    symbols.pop
                }
            }
            
        }
            
        symbols.isEmpty
        
    }
    
    def main(args: Array[String]) {
        val expressions = readInput()
        expressions.foreach { x => println(if (validate(x)) "YES" else "NO") }
    }
}
