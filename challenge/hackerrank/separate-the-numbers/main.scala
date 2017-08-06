object Solution {
  import scala.collection._

  
  def readInput(): List[String] = {
      val sc = new java.util.Scanner (System.in)
      val n = sc.nextInt()
      val array = new Array[String](n)
      for(i <- 0 until n) {
        array(i) = sc.next()
      }
      array.toList
  }

  def toBeautiful(str: String): Option[Long] = {
    def starts(str: String): Seq[Int] = {
      val len = str.length / 2
      for (i <- 1 to len) yield i
    }

    def beautify(str: String, start: Long): Boolean = {
      if (str.isEmpty) return true

      val nextInt = start + 1
      val next = nextInt.toString
      if (!str.startsWith(next)) false
      else beautify(str.substring(next.length), nextInt)
    }
    
    starts(str).find( { num => 
      val (head, tail ) = str.splitAt(num)      
      beautify(tail, head.toLong) 
    }).map( num => { str.substring(0, num).toLong })
  }
  
  def mkString(b: Option[Long]): String =  b match {
    case Some(s) => s"YES $s"
    case None => "NO"
  }
  
  def main(args: Array[String]) {
    val cases = readInput()
    cases.foreach { c => println(mkString(toBeautiful(c))) }
  }
}
