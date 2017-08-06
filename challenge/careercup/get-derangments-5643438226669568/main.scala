object Solution {

  def readInput(): String = {
    val sc = new java.util.Scanner(System.in)
    sc.next
  }

  def getDerangements(str: String): List[String] = {
    def getIndex(str: String): Set[(Char, Int)] = {
      str.zipWithIndex.toSet
    }

    def permutate(ch: Char, str: String): List[String] = {
      (for (i <- 0 to str.length) yield {
        val (first, second) = str.splitAt(i)
        first + ch + second
      }).toList
    }

    def permutations(s: String): List[String] = {
      if (s.isEmpty) Nil
      else {
        val ch = s.head
        val rest = s.tail
        permutations(rest) match {
          case Nil => List(ch.toString)
          case d: List[String] => d.flatMap(permutate(ch, _))
        
        }
      }
    }

    def isDerangement(s: String, index: Set[(Char, Int)]): Boolean = {
      val ind = getIndex(s)
      !(ind & index).isEmpty
    }
    
    val index = getIndex(str)

    permutations(str).filter(!isDerangement(_, index)).toSet.toList
   
  }

  def main(args: Array[String]) {
    val str = readInput()
    getDerangements(str).foreach(println(_))
  }

}
