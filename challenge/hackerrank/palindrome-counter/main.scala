object Solution {
  import scala.collection._
  
  def input: List[String] = {
    "tacocat" :: Nil
  }


  def solve(str: String): Int = {

    def isPalindrom(x: Int, y: Int): Boolean = {
        x < y && (0 to (y - x) / 2).foldLeft[Boolean](true) { (acc, i) =>
          acc && str(x + i) == str(y - 1 - i)
        }
      }

    def count(): Int = {
      (for {
        i <- 0 to str.length
        j <- i to str.length
      } yield if (isPalindrom(i, j)) 1 else 0).sum
      
    }
    count()
  }

  

  def main(args: Array[String]) = {
     
    input.foreach { c =>
      println(s"${solve(c)}")
    }
  }
}
