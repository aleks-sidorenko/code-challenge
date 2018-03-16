object Solution {
  import scala.collection._
  import scala.util._


  val familySize = 3

  type Case = (Int, String)
  type Row = Int
  type Place = Int  
  type Seat = (Row, Place)
  type Result = Int
  type Group  = Seq[Seat]

  def readInput(): List[Case] = {

    val sc = new java.util.Scanner(System.in)

    val t = sc.nextInt
    val cases = new Array[Case](t)
    for (i <- 0 until t) {
      val n = sc.nextInt
      val reserved = sc.nextLine
      cases(i) = n -> reserved
    }
    
    cases.toList
  }

  def solve(n: Int, reserved: String): Result = {
    def positionFromChar(ch: Char, base: Char): Place = ch - base

    def parseSeat(raw: String): Option[Seat] = if (raw.length == 2) Some(positionFromChar(raw(1), '1') -> positionFromChar(raw(0), 'A')) else None
    def parseSeats(reserved: String): List[Seat] = reserved.toUpperCase.split(" ").flatMap(parseSeat(_)).toList

    val reservedSeats = parseSeats(reserved).toSet

    def isFree(group: Group): Boolean = {
      (0 to (group.size - familySize)).foldLeft(false) { case (acc, i) =>
        acc || (group.drop(i).take(familySize).toSet & reservedSeats).isEmpty
      }
    }

    def freeGroups(row: Row): Result = {
      val groups: Seq[Group] = ((0 to 2) :: (3 to 6) :: (7 to 9) :: Nil).map(_.map(row -> _))
      groups.map(isFree(_)).filter(identity).size
    }

    (0 until n).map(freeGroups(_)).sum
  }


  def main(args: Array[String]) = {
    val cases = readInput()
    cases.foreach { case (n, reserved) =>
      println(s"${Solution.solve(n, reserved)}")
    }
  }
}
