object Solution {
  import scala.collection._
  

  def input(): List[(Array[Int], Int)] = {
    Array(-4, -2, 1, -3) -> 2 :: 
    Array(4, -2, 1, 1) -> 2 :: 
    Array(1, 1, 1, 1) -> 2 :: 
    Array(1, -1, 7, -2, 4) -> 3 :: 
    Nil
  }

  def solve(cs: (Array[Int], Int)): List[Int] = {
    val (nums, k) = cs

    val initSum = nums.take(k).sum
    val (_, _, start) = (k until nums.length).foldLeft((initSum, initSum, 0)) { case ((sum, maxSum, x), i) =>      
      val newSum = sum + nums(i) - nums(i - k)
      if (maxSum >= newSum) (newSum, maxSum, x)
      else (newSum, newSum, i - k + 1)
    } 

    nums.slice(start, start + k).toList
  }

  
  def main(args: Array[String]) = {
    val cases = input() 
    cases.foreach { c =>
      println(s"${solve(c)}")
    }
  }
}
