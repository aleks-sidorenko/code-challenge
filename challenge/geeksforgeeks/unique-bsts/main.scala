object Solution {
  import scala.collection._
  

  def readInput(): List[Int] = {
    2 :: 3  :: Nil
  }

  def solution(t: Int): Int = {
    t
  }

  

  def main(args: Array[String]) = {
    val cases = readInput()
   
    cases.foreach { c =>
      println(s"${solution(c)}")
    }
  }
}
