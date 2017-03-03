package captify.test.scala

import scala.collection.immutable.Seq
import scala.collection.mutable
import scala.util.{Failure, Success, Try}

/**
  * Here are the functions to fill in.
  */
object TestAssignment {
  /**
    * Generate a contiguous sub-sample from given sequence.
    *
    * Iterator provided should be immediately thrown away after calling this method,
    * so don't worry about any side-effects.
    *
    * @param iterator to be sampled
    * @param after the index of first element to be included, zero-based
    * @param sampleSize quantity of elements returned
    * @return sampleAfter(iteratorFromOne, 1, 2) should be same as to Seq[BigInt](2,3).toIterator
    */
  def sampleAfter(iterator: Iterator[BigInt], after: Int, sampleSize: Int): Iterator[BigInt] =
    iterator.drop(after).take(sampleSize)

  /**
    * Get value by index from given iterator.
    *
    * In case Iterator does not provide enough elements, an exception should be thrown.
    *
    * Iterator provided should be immediately thrown away after calling this method,
    * so don't worry about any side-effects.
    *
    * @param iterator to get value from
    * @param position zero-based
    * @return value at given position
    */
  def valueAt(iterator: Iterator[BigInt], position: Int): BigInt = iterator.drop(position).next()

  /**
    * Produce an iterator which generates values from given subset of input iterators of non-descending streams.
    *
    * The iterator returned should conform to following properties:
    * * output iterator is producing values in non-descending order (as all input iterators do)
    * * duplicates are allowed:
    *   * if there're occurrences of the same value across multiple iterators -
    *       respective number of dupes are present in merged version
    *   * if there're any dupes present in one of input iterators -
    *       respective number of dupes are present in merged version
    *
    * @param iterators to be merged
    * @return Iterator with all elements and non-descending ordering retained
    */
  def mergeIterators(iterators: Seq[Iterator[BigInt]]): Iterator[BigInt] = {
    new Iterator[BigInt] {

      private[this] implicit object Ord extends Ordering[BufferedIterator[BigInt]] {
        import scala.math.Ordering._
        override def compare(x: BufferedIterator[BigInt], y: BufferedIterator[BigInt]): Int =
          implicitly(Ordering[BigInt]).compare(y.head, x.head)
      }

      private[this] val queue: mutable.PriorityQueue[BufferedIterator[BigInt]] =
        mutable.PriorityQueue(iterators.map(_.buffered): _*)

      def hasNext(): Boolean = queue.headOption match {
        case Some(head) => head.hasNext || { queue.dequeue(); hasNext()}
        case _ => false
      }

      def next(): BigInt =
        if (hasNext) {
          val head = queue.dequeue()
          val next = head.next()
          // check if iterator is not empty
          if (head.hasNext) {
            // enqueue it again
            queue enqueue head
          }
          next

        } else {
            Iterator.empty.next()
        }
    }
  }

  /**
    * How much elements, on average, do have multiple of sparsity bits?
    *
    * For example for sparsity=3 how probable is it that a number has 3,6,9,12 and so on bits?
    *
    * @param sparsity to analyze, must be > 0
    * @param extent number of sequence elements to analyze, must be > 1
    * @return approximately 0.5 for sparsity=2, 0.33 for sparsity=3, and so on
    */
  def approximateSparsity(sparsity: Int, extent: Int): Double = {
    if (extent < 2) {
      throw new IllegalArgumentException("extent should cover at least two elements")
    }
    if (sparsity < 1) {
      throw new IllegalArgumentException("sparsity should be positive")
    }

    val iterators: Seq[Iterator[BigInt]] =
      (1 to 256)
        .map(_ * sparsity)
        .map(SparseIterators.iteratorForBitCount)

    val merged: Iterator[BigInt] =
      mergeIterators(iterators)

    val sequenceStart: Double = merged.next().doubleValue()
    val sequenceEnd: Double = valueAt(merged, extent - 1).doubleValue()

    extent / (sequenceEnd - sequenceStart)
  }

  /**
    * Approximate actual for given range of sparsity values.
    *
    * As approximation is potentially long-running task, try to run calls to approximateSparsity() in parallel.
    * Also, as such calls may end up in exception, actual estimation values should be kept in Try.
    *
    * For example, calling this with sparsityMin=2, sparsityMax=4, extent=1000 should:
    * - incur three calls to approximateSparsity for three respective values of sparsity and extent of 1000
    * - return Seq(2 -> Success(0.5), 3 -> Success(0.33), 4 -> Success(0.25)) (values given are approximates)
    *
    * @param sparsityMin non-negative value, inclusive for the range evaluated
    * @param sparsityMax non-negative value, inclusive for the range evaluated
    * @param extent this affects precision and time spent
    *
    * @return Seq of (Sparsity, Try[Double]) pairs
    */
  def approximatesFor(sparsityMin: Int, sparsityMax: Int, extent: Int): Seq[(Int, Try[Double])] = {
    val sparsities = sparsityMin to sparsityMax

    // using parallel collection here (ForkJoinPool by default)
    sparsities.par.map { s =>
      try {
        s -> Success(approximateSparsity(s, extent))
      }
      catch {
        case e: Throwable => s -> Failure(e)
      }
    } seq
  }


}
