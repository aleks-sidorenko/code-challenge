

object Solution {

  def readInput(): (Int, List[(Int, Int)]) = {
    val sc = new java.util.Scanner(System.in)
    var N = sc.nextInt()
    assert(N >= 1)

    var I = sc.nextInt()
    assert(I >= 1)

    val pairs = new scala.collection.mutable.ArrayBuffer[(Int, Int)](I)

    for (i <- 0 to I - 1) {
      val p = (sc.nextInt(), sc.nextInt())
      pairs += p
    }

    (N, pairs.toList)
  }

  def getCountries(pairs: List[(Int, Int)], n: Int): List[Int] = {

    def merge(countries: List[Set[Int]], pair: (Int, Int)):
    List[Set[Int]] = {
      val (intersects, other) = countries.partition(c => c.contains(pair._1) || c.contains(pair._2))
      val tail = intersects.foldLeft(Set.empty[Int])(_ ++ _) ++ Set(pair._1, pair._2)
      tail :: other
    }

    val paired = pairs.foldLeft(List.empty[Set[Int]])((cs, p) => merge(cs, p)).toList.map(_.size)

    val nonPaired = List.fill(n - paired.sum)(1)

    paired ::: nonPaired
  }

  def calculateWays(countries: List[Int]): BigInt = {

    val sum = countries.sum

    def calc(cs: List[Int], sum: BigInt): BigInt = {
      cs match {
        case Nil => 0
        case head :: tail => head * (sum - head) + calc(tail, sum - head)
      }
    }

    calc(countries, sum)
  }

  def main(args: Array[String]) {
    val (n, pairs) = readInput()
    val countries = getCountries(pairs, n)

    println(calculateWays(countries))
  }
}