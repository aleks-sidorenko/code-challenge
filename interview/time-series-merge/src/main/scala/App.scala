package timeseriesmerge


import java.io.FileWriter

import scala.collection._
import scala.io.Source
import scala.util.Try


case class Record(date: String, value: Int) {

  def line = s"$date${Record.Delimiter}$value\n"
}

object Record {
  private final val Delimiter = ':'

  implicit val ordering: Ordering[Record] = Ordering.by(_.date)

  def parse(str: String): Option[Record] = {
    val parts = str.split(Delimiter)
    if (parts.length != 2) return None
    val (date, value) = (parts(0), parts(1))
    Try(value.toInt).toOption.map(v => Record(date, v))
  }
}

object Iterators {


  final class ParsingIterator(iterator: Iterator[String]) extends AbstractIterator[Record] {

    override def hasNext: Boolean = iterator.hasNext

    override def next(): Record = {
      Record.parse(iterator.next()) match {
        case Some(r) => r
        case _ => next()
      }
    }
  }

  final class GroupingIterator(iterator: Iterator[Record]) extends AbstractIterator[Record] {

    private[this] val buffered = iterator.buffered

    override def hasNext: Boolean = {
      buffered.hasNext
    }

    override def next(): Record = {
      val next = buffered.next
      val date = next.date
      var value = next.value

      // aggregate while date is same
      while (buffered.headOption.isDefined && buffered.head.date == date) {
        val current = buffered.next
        value += current.value
      }
      if (value != next.value) Record(date, value) else next
    }
  }

  final class MergingIterator(iterators: Seq[Iterator[Record]]) extends AbstractIterator[Record] {

    type RecordIterator = BufferedIterator[Record]

    private[this] implicit val ordering: Ordering[RecordIterator] =
      (x: RecordIterator, y: RecordIterator) => implicitly[Ordering[Record]].compare(y.head, x.head)

    private[this] val queue: mutable.PriorityQueue[BufferedIterator[Record]] =
      mutable.PriorityQueue(iterators.map(_.buffered): _*)

    def hasNext(): Boolean = queue.headOption match {
      case Some(head) => head.hasNext || {
        queue.dequeue();
        hasNext()
      }
      case _ => false
    }

    def next(): Record = {
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

}


object Merger {

  import Iterators._

  def merge(iterators: Seq[Iterator[Record]]): Iterator[Record] = {
    new GroupingIterator(new MergingIterator(iterators))
  }

}

object App {

  type File = java.io.File

  case class Config(dir: String = "", output: String = "")

  def process(dir: String, out: String): Unit = {
    val iterators = new File(dir)
      .listFiles
      .filter(_.isFile)
      .map(Source.fromFile(_).getLines())
      .map(new Iterators.ParsingIterator(_))

    val merged = Merger.merge(iterators)

    // write output iterator to file
    val writer = new FileWriter(new File(out))
    merged.foreach(record => writer.write(record.line))
    writer.close()
  }

  def main(args: Array[String]): Unit = {


    val parser = new scopt.OptionParser[Config]("Time Series Merger") {

      opt[String]('d', "dir").required().valueName("<dir>")
        .action((x, c) => c.copy(dir = x)).text("directory with input files to merge")

      opt[String]('o', "out").required().valueName("<file>").
        action((x, c) => c.copy(output = x)).text("output file")
    }

    parser.parse(args, Config()) match {
      case Some(config) =>
        process(config.dir, config.output)

      case _ =>

    }
  }
}
