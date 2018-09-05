object Solution {
  import scala.annotation.tailrec
  import scala.collection._
  import cats.effect.IO
  import cats.implicits._
  import cats.data._

  def putStrlLn(value: String) = IO(println(value))
  val readLn = IO(scala.io.StdIn.readLine)

  def readInput(): IO[List[String]] = {
    for {
      n <- readLn.map(_.toInt)
      c <- (1 to n).toList.map(_ => readLn).sequence
    } yield c
  }
  
  
  def solve(input: String): String = {
    type Digit = Int
    type Hex = Int
    type ParserState = StateT[Option, List[Digit], Char]

    val digits = input.map(_.asDigit).toList

    def toChar(v: Hex) = v.toChar

    val parserState = StateT[Option, List[Digit], Char] {
        case d1 :: d2 :: d3 :: ds if d1 == 1 => Some(ds -> toChar(d1 * 100 + d2 * 10 + d3))
        case d1 :: d2 :: ds => Some(ds -> toChar(d1 * 10 + d2))
        case _ => None
      }

    
    def parse(p: ParserState, digits: List[Digit]): List[Char] = {
      @tailrec
      def loop(ds: List[Digit], acc: List[Char]): List[Char] = {
        p.run(ds) match {
          case Some((s1, c)) => loop(s1, c :: acc)
          case _ => acc
        }
      }

      loop(digits, Nil).reverse
    }

    parse(parserState, digits).mkString
  }

  def main(args: Array[String]) = {
    val program: IO[Unit] =
      for {
        cases <- readInput
        _ <- cases.map(c => putStrlLn(solve(c))).sequence
      } yield ()

    program.unsafeRunSync()

  }
}
