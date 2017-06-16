package calculator

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import org.scalatest._

import TweetLength.MaxTweetLength

@RunWith(classOf[JUnitRunner])
class CalculatorSuite extends FunSuite with ShouldMatchers {

  /******************
   ** TWEET LENGTH **
   ******************/

  def tweetLength(text: String): Int =
    text.codePointCount(0, text.length)

  test("tweetRemainingCharsCount with a constant signal") {
    val result = TweetLength.tweetRemainingCharsCount(Var("hello world"))
    assert(result() == MaxTweetLength - tweetLength("hello world"))

    val tooLong = "foo" * 200
    val result2 = TweetLength.tweetRemainingCharsCount(Var(tooLong))
    assert(result2() == MaxTweetLength - tweetLength(tooLong))
  }

  test("tweetRemainingCharsCount with a supplementary char") {
    val result = TweetLength.tweetRemainingCharsCount(Var("foo blabla \uD83D\uDCA9 bar"))
    assert(result() == MaxTweetLength - tweetLength("foo blabla \uD83D\uDCA9 bar"))
  }


  test("colorForRemainingCharsCount with a constant signal") {
    val resultGreen1 = TweetLength.colorForRemainingCharsCount(Var(52))
    assert(resultGreen1() == "green")
    val resultGreen2 = TweetLength.colorForRemainingCharsCount(Var(15))
    assert(resultGreen2() == "green")

    val resultOrange1 = TweetLength.colorForRemainingCharsCount(Var(12))
    assert(resultOrange1() == "orange")
    val resultOrange2 = TweetLength.colorForRemainingCharsCount(Var(0))
    assert(resultOrange2() == "orange")

    val resultRed1 = TweetLength.colorForRemainingCharsCount(Var(-1))
    assert(resultRed1() == "red")
    val resultRed2 = TweetLength.colorForRemainingCharsCount(Var(-5))
    assert(resultRed2() == "red")
  }


  test("computeSolutions with multiple roots") {
    val (a, b, c) = (Var(1d), Var(1d), Var(-6d))
    val delta = Polynomial.computeDelta(a, b, c)
    val result = Polynomial.computeSolutions(a, b, c, delta)

    assert(result() == Set(2, -3))

  }

  test("computeSolutions with same roots") {
    val (a, b, c) = (Var(1d), Var(0d), Var(-1d))
    val delta = Polynomial.computeDelta(a, b, c)
    val result = Polynomial.computeSolutions(a, b, c, delta)

    assert(result() == Set(1, -1))

  }

  test("computeValues with complex expression") {
    val (a, b, c) = (Literal(1.0d), Literal(2.0d), Literal(3.0d))
    val (d, e, f) = (Ref("a"), Ref("b"), Ref("c"))

    val g = Times(Plus(a, d), b) // 4
    val h = Divide(Minus(a, f), b) // -1

    val expressions: Map[String, Signal[Expr]] = Map("a" -> Var(a), "b" -> Var(b), "c" -> Var(c),
      "d" -> Var(d), "e" -> Var(e), "f" -> Var(f), "g" -> Var(g), "h" -> Var(h))

    val expected: Map[String, Double] = Map("a" -> 1.0d, "b" -> 2.0d, "c" -> 3.0d,
      "d" -> 1.0d, "e" -> 2.0d, "f" -> 3.0d, "g" -> 4.0d, "h" -> -1.0d)

    val result = Calculator.computeValues(expressions).mapValues(e => e())

    assert(result == expected)

  }

  test("computeValues with cyclic dependencies") {

    val a = Plus(Ref("b"), Literal(1.0d))
    val b = Times(Ref("a"), Literal(2.0d))

    val expressions: Map[String, Signal[Expr]] = Map("a" -> Var(a), "b" -> Var(b))

    val result = Calculator.computeValues(expressions).mapValues(e => e())

    assert ( result("a").isNaN )
    assert ( result("b").isNaN )

  }

  test("computeValues with non existend dependencies") {

    val a = Plus(Ref("b"), Literal(1.0d))
    val b = Times(Ref("c"), Literal(2.0d))

    val expressions: Map[String, Signal[Expr]] = Map("a" -> Var(a), "b" -> Var(b))

    val result = Calculator.computeValues(expressions).mapValues(e => e())

    assert ( result("a").isNaN )
    assert ( result("b").isNaN )

  }

}
