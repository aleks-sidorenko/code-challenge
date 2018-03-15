object Solution {
  import scala.collection._

  
  type Case = (Int, String)
  type Result = Int
    

  def readInput(): List[Case] = {
    (1 -> "" :: 10 -> "A1 B2" :: Nil
  }

  sealed trait Solution {
    def solve(input: Case): Result
  }


  class Solution1 extends Solution {

    final def solve(input: Case): Result = {
      1
    }
  }

  def main(args: Array[String]) = {
    val cases = readInput()
    val solution = new Solution1()
    cases.foreach { c =>
      println(s"${solution.solve(c)}")
    }
  }
}
