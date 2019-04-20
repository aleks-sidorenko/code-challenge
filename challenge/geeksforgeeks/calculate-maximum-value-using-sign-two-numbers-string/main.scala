object Solution {
  import scala.collection._
  

  def input(): List[String] = {
    "01231" :: "891" :: Nil
  }

  def solve(n: String): Int = {
  
    val nums = n.toList.map(_.asDigit)
  
    val isZeroOrOne = (x: Int) => x == 0 || x == 1
    nums.reduce { (acc, x) => 
      if (isZeroOrOne(x) || isZeroOrOne(acc)) x + acc
      else x * acc
    }
    
  }

  
  def main(args: Array[String]) = {
    val cases = input() 
    cases.foreach { c =>
      println(s"${solve(c)}")
    }
  }
}
