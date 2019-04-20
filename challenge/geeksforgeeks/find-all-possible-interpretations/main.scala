object Solution {
  import scala.collection._
  import scala.language.implicitConversions
  

  def input(): List[String] = {
    "11" :: 
    "121" ::
    "918" ::
    Nil
  }

  def solve(cs: String): List[String] = {
    
    val alphabet = ('a' to 'z').map { x => (x.toInt - 'a'.toInt + 1).toString -> x}.toMap

    implicit def char2Str(ch: Char): String = ch.toString
    
    def loop(left: List[Char], acc: List[Char], all: List[String]): List[String] = {
      left match {
        case x1 :: x2 :: xs if alphabet.contains(x1) && alphabet.contains(x1 + x2) =>
          val res1 = loop(x2 :: xs, alphabet(x1) :: acc, all)
          loop(xs, alphabet(x1 + x2) :: acc, res1)

        case x1 :: xs if alphabet.contains(x1) => loop(xs, alphabet(x1) :: acc, all)
        case _ => acc.reverse.mkString :: all
      }
    }
    loop(cs.toList, List.empty[Char], List.empty[String])
  }

  
  def main(args: Array[String]) = {
    val cases = input() 
    cases.foreach { c =>
      println(s"${solve(c)}")
    }
  }
}
