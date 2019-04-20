object Solution {
  import scala.collection._
  

  def input(): List[String] = {
    "01231" :: "891" :: Nil
  }

  def solve(n: String): Int = {
    
    def loop(nums: List[Int], acc: Int): Int = {
      nums match {
        case x :: xs => math.max(loop(xs, acc * x), loop(xs, acc + x))
        case _ => acc
      }
    }

    val f :: rest = n.toList.map(_.asDigit)
    
    loop(rest, f)
  }

  
  def main(args: Array[String]) = {
    val cases = input() 
    cases.foreach { c =>
      println(s"${solve(c)}")
    }
  }
}
