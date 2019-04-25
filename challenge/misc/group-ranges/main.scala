object Solution {
  import scala.collection._
  
  def input(): List[String] = {
    "1,3,4,6-9,10,12-15,17,19,22,23,24" ::
    Nil
  }


  case class Range(from: Int, to: Int) {
    def str = if (from == to) s"$from" else s"$from-$to"
  }

  object Range {
    def minmax(r1: Range, r2: Range): (Range, Range) = 
      if (r1.from < r2.from) r1 -> r2 else r2 -> r1
    
    def merge(r1: Range, r2: Range): Option[Range] = {
      val (min, max) = minmax(r1, r2)
      if (min.to + 1 < max.from) None
      else Some(Range(min.from, max.to))
    }
    
    def apply(s: String): Range = {
      val spl = s.split('-')
      val from = spl.head.toInt
      if (spl.size == 1) Range(from, from)
      else Range(from, spl.last.toInt)
    }
  }


  def solve(in: String): String = {
    
    val ranges = in.split(',').map(Range(_)).toList

    ranges.foldRight(List.empty[Range]) { (r, acc) =>
      (for {
        head <- acc.headOption
        m <- Range.merge(head, r)  
      } yield m :: acc.tail).getOrElse(r :: acc)

    }
    .map(_.str).mkString(",")
  }

  
  def main(args: Array[String]) = {
    val cases = input() 
    cases.foreach { c =>
      println(s"${solve(c)}")
    }
  }
}
