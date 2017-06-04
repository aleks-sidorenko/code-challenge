package recfun

import scala.annotation.tailrec
import scala.collection.mutable

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
    * Exercise 1
    */
  def pascal(c: Int, r: Int): Int = {
    val length = r + 1
    if (c == 0 || c == length - 1) 1
    else pascal(c - 1, r - 1) + pascal(c, r - 1)
  }

  /**
    * Exercise 2
    */
  def balance(chars: List[Char]): Boolean = {
    if (chars.isEmpty) return true

    @tailrec
    def balanceHelper(chars: List[Char], unclosed: Int): Boolean = {
      if (unclosed < 0) false
      else if (chars.isEmpty) unclosed == 0
      else {
        val cur = chars.head match {
          case '(' => 1
          case ')' => -1
          case _ => 0
        }
        balanceHelper(chars.tail, unclosed + cur)
      }
    }

    balanceHelper(chars, 0)
  }

  /**
    * Exercise 3
    */
  def countChange(money: Int, coins: List[Int]): Int = {

    if (coins.isEmpty && money > 0) return 0

    val cache = new mutable.HashMap[String, Int]()

    def cacheKey(money: Int, coins: List[Int]): String = s"${money}_${coins.mkString}"

    def countHelper(money: Int, coins: List[Int]): Int = {
      if (money == 0) return 1
      if (money < 0) return 0

      cache.getOrElseUpdate(cacheKey(money, coins), coins.map(c => countHelper(money - c, coins.filter(_ >= c))).sum)
    }

    countHelper(money, coins)
  }
}
