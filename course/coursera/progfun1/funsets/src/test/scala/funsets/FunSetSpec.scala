package funsets

import org.scalatest.{FlatSpec, Matchers}

class FunSetSpec extends FlatSpec with Matchers {

  import FunSets._

  object testSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val s4 = singletonSet(4)
  }

  "contains" should "be implemented" in {
    contains(_ => true, 100) should be (true)
  }


  "singletonSet(1)" should "contain 1" in {
    contains(testSets.s1, 1) should be (true)
  }

  it should "not contain 100" in {
    contains(testSets.s1, 100) should be (false)
  }

  it should "not contain -200" in {
    contains(testSets.s1, -200) should be (false)
  }


  "union" should "contain all elements of each sets" in {

    val s = union(union(testSets.s1, testSets.s2), testSets.s3)
    contains(s, 1) should be (true)
    contains(s, 2) should be (true)
    contains(s, 3) should be (true)
    contains(s, 4) should be (false)
    contains(s, -2) should be (false)
  }

  it should "contain all elements single set" in {
    val s = union(testSets.s1, testSets.s1)
    contains(s, 1) should be (true)
    contains(s, 2) should be (false)
  }

  "intersect" should "contain only common elements of each sets" in {

    val s1 = union(testSets.s1, testSets.s2)
    val s2 = union(testSets.s2, testSets.s3)
    val s = intersect(s1, s2)
    contains(s, 1) should be (false)
    contains(s, 2) should be (true)
    contains(s, 3) should be (false)

    contains(s, 4) should be (false)
    contains(s, -2) should be (false)
  }

  it should "be empty if sets don't have common elements" in {
    val s1 = testSets.s1
    val s2 = testSets.s2
    val s = intersect(s1, s2)
    contains(s, 1) should be (false)
    contains(s, 2) should be (false)
    contains(s, 3) should be (false)

  }

  it should "contain all elements single set" in {
    val s = intersect(testSets.s1, testSets.s1)
    contains(s, 1) should be (true)
    contains(s, 2) should be (false)
  }

  "diff" should "contain only different elements from first set" in {

    val s1 = union(testSets.s1, testSets.s2)
    val s2 = union(testSets.s2, testSets.s3)
    val s = diff(s1, s2)
    contains(s, 1) should be (true)
    contains(s, 2) should be (false)
    contains(s, 3) should be (false)
    contains(s, 4) should be (false)
    contains(s, -2) should be (false)
  }

  it should "be empty for single set" in {
    val s1 = testSets.s1
    val s2 = testSets.s1
    val s = diff(s1, s2)
    contains(s, 1) should be (false)
    contains(s, 2) should be (false)
    contains(s, 3) should be (false)

  }

  it should "contain all elements if sets don't have common elements" in {
    val s1 = union(testSets.s1, testSets.s3)
    val s2 = testSets.s2
    val s = diff(s1, s2)
    contains(s, 1) should be (true)
    contains(s, 3) should be (true)
    contains(s, 2) should be (false)
  }

  "forall" should "contain only even elements" in {
    val even = (x: Int) => x % 2 == 0
    val s1 = union(union(testSets.s1, testSets.s2), testSets.s3)
    forall(s1, even) should be (false)

    val s2 = union(testSets.s2, testSets.s4)
    forall(s2, even) should be (true)
  }


  "exists" should "be true if even element is present" in {
    val even = (x: Int) => x % 2 == 0
    val s1 = union(union(testSets.s1, testSets.s2), testSets.s3)
    exists(s1, even) should be (true)

  }

  it should "be false for non-even elements" in {
    val even = (x: Int) => x % 2 == 0
    val s2 = union(testSets.s1, testSets.s3)
    exists(s2, even) should be (false)
  }

  "map" should "double elements" in {
    val f = (x: Int) => 2 * x
    val s2 = union(testSets.s1, testSets.s3)
    val s3 = map(s2, f)

    contains(s3, 1) should be (false)
    contains(s3, 2) should be (true)
    contains(s3, 3) should be (false)
    contains(s3, 6) should be (true)
  }

  it should "square elements" in {
    val f = (x: Int) => x * x
    val s = (x: Int) => math.abs(x) == 1 || math.abs(x) == 2 || x == 0
    val res = map(s, f)

    contains(res, 0) should be (true)
    contains(res, 1) should be (true)
    contains(res, 4) should be (true)
    contains(res, -1) should be (false)
    contains(res, -2) should be (false)
  }

}