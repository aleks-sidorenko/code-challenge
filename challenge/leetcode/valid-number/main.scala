object Solution {
    import scala.util._
    def isNumber(s: String): Boolean = {
        val validChars: Set[Char] = ('e' :: '+' :: '-' :: '.' :: ('0' to '9').toList).toSet
        
        val input = s.trim()
        input.forall { validChars(_) } && Try { input.toDouble }.isSuccess
        
    }
}