package captify.test.scala

import scala.collection.convert.WrapAsScala.asScalaIterator
import captify.test.java.SparseIterators.{ iteratorForBitCount => javaIteratorForBitCount }

object SparseIterators {
  def iteratorForBitCount(bitCount: Int): Iterator[BigInt] =
    for {
      bigInteger <- asScalaIterator(javaIteratorForBitCount(bitCount))
    } yield {
      //  trigger the implicit, wrapping the java.math.BigInteger into scala.math.BigInt
      bigInteger: BigInt
    }
}
