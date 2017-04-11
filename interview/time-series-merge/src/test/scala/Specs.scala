package timeseriesmerge

import org.scalatest.{FlatSpec, Matchers}

import scala.io.Source

class MergerSpec extends FlatSpec with Matchers {

  "Merger" should "merge iterators with different records" in {
    val iter1 = Seq(Record("2017-01-01", 2), Record("2017-03-02", 3)).toIterator
    val iter2 = Seq(Record("2017-02-01", 2), Record("2017-02-02", 3)).toIterator
    val expected = List(
      Record("2017-01-01", 2),
      Record("2017-02-01", 2),
      Record("2017-02-02", 3),
      Record("2017-03-02", 3)
    )

    val result = Merger.merge(List(iter1, iter2))
    result.toList should be (expected)
  }

  it should "merge iterators with some duplicates" in {
    val iter1 = Seq(Record("2017-01-01", 1), Record("2017-02-02", 1), Record("2017-03-02", 2)).toIterator
    val iter2 = Seq(Record("2017-01-01", 1), Record("2017-02-01", 3), Record("2017-02-02", 1)).toIterator
    val iter3 = Seq(Record("2017-01-01", 2), Record("2017-02-02", 4), Record("2017-03-02", 3)).toIterator
    val iter4 = Seq(Record("2017-01-01", 3), Record("2017-03-01", 5)).toIterator

    val expected = List(
      Record("2017-01-01", 5),
      Record("2017-02-01", 5),
      Record("2017-02-02", 5),
      Record("2017-03-01", 5),
      Record("2017-03-02", 5)
    )

    val result = Merger.merge(List(iter1, iter2, iter3))
    result.toList should be (expected)
  }



}
