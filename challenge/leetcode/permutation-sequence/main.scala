object Solution {
    def getPermutation(n: Int, k: Int): String = {
        
        def memoize[I, O](f: I => O): I => O = new collection.mutable.HashMap[I, O]() {
            override def apply(key: I) = getOrElseUpdate(key, f(key))
        }                
        
        lazy val fac: ((Int, Int)) => Int = memoize {
            case (0, acc) => acc
            case (l, acc) => fac(l - 1, acc * l)
        }
        
        def fact(n: Int): Int = fac(n, 1)
        
        def loop(k: Int, nums: List[Int], res: List[Int]): List[Int] = {            
            nums match {
                case x1 :: x2 :: xs => 
                    val fc = fact(nums.size - 1)   
                    val y = if (k % fc != 0) k / fc else k / fc -1
                    val next = nums(y)
                    loop(k - y * fc, nums.filterNot(_ == next), next :: res)
                case x :: Nil => 
                    loop(0, Nil, x :: res)
                    
                case _ => res.reverse
            }
        }
        
        loop(k, (1 to n).toList, List.empty).mkString
    }
}