object Solution {
  import scala.collection._

  def readInput(): (List[String], List[String]) = {
    val sc = new java.util.Scanner(System.in)
    sc.useDelimiter(System.getProperty("line.separator"));
    val target = sc.next().split(' ').map(_.toLowerCase).toList
    val all = sc.next().split(' ').map(_.toLowerCase).toList
    (target, all)
  }


  case class Solution(target: Set[String], all: List[String]) {
    final def solve(): Option[(Int, Int)] = {
      val zipped = all.zipWithIndex.groupBy(_._1).mapValues(_.map(_._2))
      if (target.diff(zipped.keySet).isEmpty) {
        val indexes = target.map(t => zipped(t)).toList
        Some(step(indexes, Nil))
      } else {
        None
      }
    }

    private def step(indexes: List[List[Int]], result: List[Int]): (Int, Int) = {
      indexes match {
        case head :: tail => 
          head.map(h => step(tail, h :: result)).minBy { case(min, max) => max - min }
        case _ => (result.min, result.max)
      }
    }

  }

  def main(args: Array[String]) = {
    val (target, all) = readInput()

    val solution = Solution(target.toSet, all)
    solution.solve() match {
      case Some((start, end)) => println(s"$start $end")
      case None => println(0)
    }    
  }
}
