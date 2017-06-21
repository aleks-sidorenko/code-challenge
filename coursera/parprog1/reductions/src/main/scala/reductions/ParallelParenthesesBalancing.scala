package reductions

import scala.annotation._
import org.scalameter._
import common._

object ParallelParenthesesBalancingRunner {

  @volatile var seqResult = false

  @volatile var parResult = false

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 40,
    Key.exec.maxWarmupRuns -> 80,
    Key.exec.benchRuns -> 120,
    Key.verbose -> true
  ) withWarmer(new Warmer.Default)

  def main(args: Array[String]): Unit = {
    val length = 100000000
    val chars = new Array[Char](length)
    val threshold = 10000
    val seqtime = standardConfig measure {
      seqResult = ParallelParenthesesBalancing.balance(chars)
    }
    println(s"sequential result = $seqResult")
    println(s"sequential balancing time: $seqtime ms")

    val fjtime = standardConfig measure {
      parResult = ParallelParenthesesBalancing.parBalance(chars, threshold)
    }
    println(s"parallel result = $parResult")
    println(s"parallel balancing time: $fjtime ms")
    println(s"speedup: ${seqtime / fjtime}")
  }
}

object ParallelParenthesesBalancing {

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
   */
  def balance(chars: Array[Char]): Boolean = {

    def extra(char: Char): Int =
      if (char == ')') -1
      else if (char == '(') 1
      else 0

    @tailrec
    def reduce(chars: List[Char], opened: Int): Boolean = {
      if (opened < 0) false
      else chars match {
        case ch :: chs => reduce(chs, opened + extra(ch))
        case _ => opened == 0
      }
    }

    reduce(chars.toList, 0)
  }

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
   */
  def parBalance(chars: Array[Char], threshold: Int): Boolean = {
    import common._

    @tailrec
    def traverse(from: Int, until: Int, opened: Int, disbalance: Int): (Int, Int) = {

      if (from >= until) opened -> disbalance
      else {
        val ch = chars(from)
        val next = from + 1
        ch match {
          case '(' => traverse(next, until, opened + 1, disbalance)
          case ')' =>
            if (opened > 0) traverse(next, until, opened - 1, disbalance)
            else traverse(next, until, opened, disbalance + 1)
          case _ => traverse(next, until, opened, disbalance)
        }
      }
    }

    def reduce(from: Int, until: Int): (Int, Int) = {
      if (until - from <= threshold) traverse(from, until, 0, 0)
      else {
        val middle = (from + until) / 2
        val ((opened1, disbalance1), (opened2, disbalance2)) =
          parallel(reduce(from, middle), reduce(middle, until))

        // now we need to check if disbalance2 is compensated by opened1
        if (opened1 >= disbalance2)
          (opened1 - disbalance2 + opened2) -> disbalance1
        else
          (opened2) -> (disbalance1 + disbalance2 - opened1)
      }
    }

    reduce(0, chars.length) == (0, 0)
  }

  // For those who want more:
  // Prove that your reduction operator is associative!

}
