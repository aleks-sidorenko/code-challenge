object Solution {
    def multiply(num1: String, num2: String): String = {
        val zero = List(0.toByte)
        def strToList(n: String): List[Byte] = n.toList.reverse.map(x => (x -'0').toByte)
        def listToStr(n: List[Byte]): String = n.reverse.mkString
        
        def reduce(n1: List[Byte]): List[Byte] = { 
            val r = n1.reverse.dropWhile(_ == 0).reverse
            if (r.isEmpty) zero else r
        }
        
        def add(n1: List[Byte], n2: List[Byte]): List[Byte] = {            
            def loop(n1: List[Byte], n2: List[Byte], res: List[Byte], extra: Byte = 0): List[Byte] = {
                val ex = (y: Byte) => if (y >= 10) 1.toByte else 0.toByte                
                (n1, n2) match {
                    case (x1 :: xs1, x2 :: xs2) => 
                        val y = (x1 + x2 + extra).toByte
                        loop(xs1, xs2, (y % 10).toByte :: res, ex(y))
                    case (Nil, x2 :: xs2) => 
                        val y = (x2 + extra).toByte
                        loop(Nil, xs2, (y % 10).toByte :: res, ex(y))
                    case (x1 :: xs1, Nil) => 
                        val y = (x1 + extra).toByte
                        loop(xs1, Nil, (y % 10).toByte :: res, ex(y))
                    case _ if extra != 0 => extra :: res
                    case _ => res
                        
                }    
            }            
            loop(n1, n2, List.empty).reverse
            
        }
        
        def mult(n1: List[Byte], n2: Byte): List[Byte] = {            
            def loop(n1: List[Byte], res: List[Byte], extra: Byte = 0): List[Byte] = {
                val ex = (y: Byte) => if (y >= 10) (y / 10).toByte else 0.toByte                
                n1 match {
                    case x1 :: xs1 => 
                        val y = (x1 * n2 + extra).toByte
                        loop(xs1, (y % 10).toByte :: res, ex(y))                    
                    case _ if extra != 0 => extra :: res
                    case _ => res
                        
                }    
            }                        
            loop(n1, List.empty).reverse
        }
        
        val n1: List[Byte] = strToList(num1)
        val n2: List[Byte] = strToList(num2)
        
        val (res, _) = n1.foldLeft(List.empty[Byte], List.empty[Byte]) { case ((acc, zeros), n) =>             
            add(acc, zeros ::: mult(n2, n)) -> (0.toByte :: zeros)
        }
        
        listToStr(reduce(res))        
    }
}