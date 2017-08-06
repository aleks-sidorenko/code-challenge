package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {


  private def sorted(h: H): List[A] = {
    def loop(h: H, l: List[A]): List[A] =
      if (isEmpty(h))
        l.reverse
      else {
        val m = findMin(h)
        loop(deleteMin(h), m :: l)
      }
    loop(h, Nil)
  }

  lazy val genHeap: Gen[H] = for {
    v <- arbitrary[Int]
    h <- oneOf(Gen.const(empty), genHeap)
  } yield insert(v, h)

  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)


  property("insert minimal element") = forAll { (h: H) =>
    val m = findMin(h)
    findMin(insert(m, h)) == m
  }

  property("insert 1 element to empty heap") = forAll { a: Int =>
    val h = insert(a, empty)
    findMin(h) == a
  }

  property("insert 2 elements to empty heap") = forAll { (a: Int, b: Int) =>
    val min = math.min(a, b)
    var h = insert(a, empty)
    h = insert(b, h)
    findMin(h) == min
  }

  property("insert to empty heap and deleteMin") = forAll { a: Int =>
    val h = insert(a, empty)
    val h2 = deleteMin(h)
    isEmpty(h2)
  }

  property("heap should form sorted list") = forAll { (h: H) =>
    val list = sorted(h)
    list.sorted == list
  }

  property("melding heaps") = forAll { (h1: H, h2: H) =>
    val min = (h: H) => findMin(h)

    val (m1, m2) = (min(h1), min(h2))
    val melded = meld(h1, h2)
    val m3 = min(melded)
    m3 == math.min(m1, m2)
  }

  property("melding heaps should form sorted list") = forAll { (h1: H, h2: H) =>

    val l1 = sorted(h1)
    val l2 = sorted(h2)
    val l = l1 ++ l2

    val melded = meld(h1, h2)
    val ml = sorted(melded)
    l.sorted == ml
  }


}
