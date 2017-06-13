package quickcheck

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import org.scalatest.prop.Checkers
import org.scalacheck.Arbitrary._
import org.scalacheck.Prop
import org.scalacheck.Prop._

import org.scalatest.exceptions.TestFailedException

object QuickCheckBinomialHeap extends QuickCheckHeap with BinomialHeap
object QuickCheckBogus1BinomialHeap extends QuickCheckHeap with Bogus1BinomialHeap
object QuickCheckBogus2BinomialHeap extends QuickCheckHeap with Bogus2BinomialHeap
object QuickCheckBogus3BinomialHeap extends QuickCheckHeap with Bogus3BinomialHeap
object QuickCheckBogus4BinomialHeap extends QuickCheckHeap with Bogus4BinomialHeap
object QuickCheckBogus5BinomialHeap extends QuickCheckHeap with Bogus5BinomialHeap

@RunWith(classOf[JUnitRunner])
class QuickCheckSuite extends FunSuite with Checkers {

  test("Binomial heap satisfies properties.") {
    check(new QuickCheckHeap with quickcheck.BinomialHeap)
  }

  test("Bogus (1) binomial heap does not satisfy properties.") {
    check(new QuickCheckHeap with quickcheck.Bogus1BinomialHeap)
  }

  test("Bogus (2) binomial heap does not satisfy properties.") {
    check(new QuickCheckHeap with quickcheck.Bogus2BinomialHeap)
  }

  test("Bogus (3) binomial heap does not satisfy properties.") {
    check(new QuickCheckHeap with quickcheck.Bogus3BinomialHeap)
  }

  test("Bogus (4) binomial heap does not satisfy properties.") {
    check(new QuickCheckHeap with quickcheck.Bogus4BinomialHeap)
  }

  test("Bogus (5) binomial heap does not satisfy properties.") {
    check(new QuickCheckHeap with quickcheck.Bogus5BinomialHeap)
  }
}
