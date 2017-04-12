object Solution {


  case class Meeting(start: Long, end: Long, weight: Int)

  def readInput(): List[Meeting] = {

    val sc = new java.util.Scanner(System.in)
    val n = sc.nextInt
    val meetings = collection.mutable.ListBuffer.empty[Meeting]
    for (i <- 0 until n) meetings += Meeting(sc.next().toLong, sc.next().toLong, sc.nextInt())
    meetings.toList
  }

  
  def main(args: Array[String]) {
    val meetings = readInput

    meetings.foreach(m => println(m))
  }
}
