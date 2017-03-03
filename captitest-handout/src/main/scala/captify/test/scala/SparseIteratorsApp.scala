package captify.test.scala

import scala.collection.immutable
import scala.util.Try

import captify.test.scala.TestAssignment._

object SparseIteratorsApp extends App {

  def runTests(sampleAfterNum: Int, sparsityMin: Int, sparsityMax: Int, approximateExtent: Int): Unit = {
    val iterators: immutable.Seq[Iterator[BigInt]] =
      Iterator
        .from(start = 2, step = 2)
        .take(512)
        .map(bits => SparseIterators.iteratorForBitCount(bits))
        .toList

    val mergeStartedAt: Long = System.currentTimeMillis()

    val mergedIterator: Iterator[BigInt] =
      mergeIterators(iterators)

    val numbers: Seq[String] =
      sampleAfter(mergedIterator, sampleAfterNum, 10)
        .map(num => s"$num ${num.bitCount}")
        .toList

    val mergeFinishedAt: Long = System.currentTimeMillis()
    val mergeMillis: Long = mergeFinishedAt - mergeStartedAt

    println(s"sampled merged iterator after $sampleAfterNum in $mergeMillis millis:\n" + numbers.mkString("\n"))

    val approximatesStartedAt: Long = System.currentTimeMillis()
    val approximatesRes: Seq[(Int, Try[Double])] = approximatesFor(sparsityMin, sparsityMax, approximateExtent)
    val approximatesFinishedAt: Long = System.currentTimeMillis()
    val approximatesMillis: Long = approximatesFinishedAt - approximatesStartedAt
    val cores: Int = Runtime.getRuntime.availableProcessors()

    println(
      s"approximate sparsities in $approximatesMillis millis by $approximateExtent elems with $cores cores:\n" +
        approximatesRes.mkString("\n")
    )

  }

  //  simple less loaded tests
  runTests(1570786, 2, 8, 100000)

  //  more intensive tests with just a bit of exceptions
  runTests(135914081, 0, 24, 10000000)
}
