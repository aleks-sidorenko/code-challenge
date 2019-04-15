object Solution {
  import scala.collection._
  

  def input(): List[Int] = {
    2 :: 3  :: 4 :: Nil
  }

  def solve(n: Int): Int = {
    if (n <= 1) 1
    else (1 to n).map(i => solve(i - 1) * solve(n - i)).sum
  }

  
  def main(args: Array[String]) = {
    val cases = input() 
    cases.foreach { c =>
      println(s"${solve(c)}")
    }
  }
}
