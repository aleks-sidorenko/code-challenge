object Solution {
  import scala.collection._
  
  type Case = (String, String)

  def input(): List[Case] = {
    ("abcd", "abdc") ::
    ("abfsfwcd", "abfdsssfc") ::
    Nil
  }

  def solve(cs: Case): String = {
    val (str1, str2) = cs

    def loop(s1: List[Char], s2: List[Char], acc: List[Char]): List[Char] = {
      (s1, s2) match {
        case (x1 :: xs1, x2 :: xs2) if x1 == x2 => loop(xs1, xs2, x1 :: acc)
        case (x1 :: xs1, x2 :: xs2) => 
          val r1 = loop(s1, xs2, acc)
          val r2 = loop(xs1, s2, acc)
          if (r1.length > r2.length) r1 else r2
        case (_, Nil) | (Nil, _) => acc
      }
    }

    loop(str1.toList, str2.toList, List.empty).reverse.mkString
  }

  
  def main(args: Array[String]) = {
    val cases = input() 
    cases.foreach { c =>
      println(s"${solve(c)}")
    }
  }
}
