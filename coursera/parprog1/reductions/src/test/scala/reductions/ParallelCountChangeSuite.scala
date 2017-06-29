package reductions

import java.util.concurrent._
import scala.collection._
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import common._

import ParallelCountChange._

@RunWith(classOf[JUnitRunner])
class ParallelCountChangeSuite extends FunSuite {

  test("moneyThreshold should return false when the money is greater than two-thirds of the starting money") {

    val threshold = moneyThreshold(3)
    assert(threshold(5, Nil) == false)
    assert(threshold(2, Nil) == true)
    assert(threshold(1, Nil) == true)

    assert(threshold(5, List(1, 2, 3)) == false)
    assert(threshold(2, List(1, 2, 3)) == true)
    assert(threshold(1, List(1, 2, 3)) == true)

  }

  test("combinedThreshold should return false when the number of coins times money greater than half of the initial number of coins times starting money") {
    val threshold = combinedThreshold(10, List(1, 2, 3, 4, 5))
    assert(threshold(7, List(1, 2, 3, 4)) == false)
  }


  test("countChange should return 0 for money < 0") {
    def check(money: Int, coins: List[Int]) = 
      assert(countChange(money, coins) == 0,
        s"countChang($money, _) should be 0")

    check(-1, List())
    check(-1, List(1, 2, 3))
    check(-Int.MinValue, List())
    check(-Int.MinValue, List(1, 2, 3))
  }

  test("countChange should return 1 when money == 0") {
    def check(coins: List[Int]) = 
      assert(countChange(0, coins) == 1,
        s"countChang(0, _) should be 1")

    check(List())
    check(List(1, 2, 3))
    check(List.range(1, 100))
  }

  test("countChange should return 0 for money > 0 and coins = List()") {
    def check(money: Int) = 
      assert(countChange(money, List()) == 0,
        s"countChang($money, List()) should be 0")

    check(1)
    check(Int.MaxValue)
  }

  test("countChange should work when there is only one coin") {
    def check(money: Int, coins: List[Int], expected: Int) =
      assert(countChange(money, coins) == expected,
        s"countChange($money, $coins) should be $expected")

    check(1, List(1), 1)
    check(2, List(1), 1)
    check(1, List(2), 0)
    check(Int.MaxValue, List(Int.MaxValue), 1)
    check(Int.MaxValue - 1, List(Int.MaxValue), 0)
  }

  test("countChange should work for multi-coins") {
    def check(money: Int, coins: List[Int], expected: Int) =
      assert(countChange(money, coins) == expected,
        s"countChange($money, $coins) should be $expected")

    check(50, List(1, 2, 5, 10), 341)
    check(250, List(1, 2, 5, 10, 20, 50), 177863)
  }

  test("parCountChange should work for multi-coins") {
    def check(money: Int, coins: List[Int], expected: Int, threshold: Threshold) =
      assert(parCountChange(money, coins, threshold) == expected,
        s"parCountChange($money, $coins) should be $expected")

    check(50, List(1, 2, 5, 10), 341, combinedThreshold(50, List(1, 2, 5, 10)))
    check(50, List(1, 2, 5, 10), 341, moneyThreshold(50))
    check(50, List(1, 2, 5, 10), 341, totalCoinsThreshold(4))

    check(250, List(1, 2, 5, 10, 20, 50), 177863, combinedThreshold(250, List(1, 2, 5, 10, 20, 50)))
    check(250, List(1, 2, 5, 10, 20, 50), 177863, moneyThreshold(250))
    check(250, List(1, 2, 5, 10, 20, 50), 177863, totalCoinsThreshold(6))
  }


}
