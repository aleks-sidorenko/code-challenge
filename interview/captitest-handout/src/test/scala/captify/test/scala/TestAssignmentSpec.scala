package captify.test.scala

import captify.test.scala.TestAssignment._
import org.scalatest._

import scala.collection.immutable._
import scala.util.Success

class TestAssignmentSpec extends FlatSpec with Matchers {

  def iter(start: Int = 1, end: Int = 10) = Range.BigInt.inclusive(start, end, 1).toIterator

  "valueAt" should "return first element" in {
    valueAt(iter(), 0) should equal(1)
  }

  it should "return last element" in {
    valueAt(iter(), 9) should equal(10)
  }

  it should "throw exception" in {
    assertThrows[NoSuchElementException](valueAt(iter(), 10))
  }

  "sampleAfter" should "sample elements" in {
    sampleAfter(iter(), 0, 2).toSeq should equal(Seq(1, 2))
    sampleAfter(iter(), 1, 2).toSeq should equal(Seq(2, 3))
  }

  it should "sample last elements" in {
    sampleAfter(iter(), 8, 2).toSeq should equal(Seq(9, 10))
  }

  it should "return empty list for out-of-range indexes" in {
    sampleAfter(iter(), 20, 2).toSeq should equal(Seq.empty[BigInt])
  }

  "mergeIterators" should "merge iterators without dups" in {
    mergeIterators(Seq(iter(1, 3), iter(5, 7))).toSeq should equal(Seq(1, 2, 3, 5, 6, 7))
  }

  it should "merge iterators with dups" in {
    mergeIterators(Seq(iter(1, 3), iter(2, 3), iter(3, 4))).toSeq should equal(Seq(1, 2, 2, 3, 3, 3, 4))
  }

  "approximatesFor" should "return valid approximation" in {
    approximatesFor(sparsityMin=2, sparsityMax=4, extent=1000) should
      equal(Seq(2 -> Success(0.5), 3 -> Success(0.33377837116154874), 4 -> Success(0.2433682161109759)))
  }

}