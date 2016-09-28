import scala.collection.immutable._

object Solution {

  def anagramDiff(a: String, b: String) : Int = {

    def makeMap(str: String) : Map[Char, Int] = {
      str.groupBy(identity).mapValues(_.size).toMap
    }

    def reduceMaps[T](map1: Map[T, Int], map2: Map[T, Int]) : Map[T, Int] = {
      val res = map1.keys.map(k => (k, Math.min(map1.getOrElse(k, 0), map2.getOrElse(k, 0)))).filter(_._2 > 0)
      res.toMap
    }

    val aMap = makeMap(a)
    val bMap = makeMap(b)

    val reduced = reduceMaps(aMap, bMap)

    val commonSize = reduced.foldLeft(0)((acc, kv) => acc + kv._2)
    a.length - commonSize + b.length - commonSize
  }

  def main(args: Array[String]) {
    val source = scala.io.Source.stdin
    val lines = source.getLines.filter(_.length > 0).toList
    val a = lines.head
    val b = lines.tail.head
    println(anagramDiff(a, b))
  }
}