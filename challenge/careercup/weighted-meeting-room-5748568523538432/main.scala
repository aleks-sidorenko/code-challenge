object Solution {

  import scala.collection.mutable.{Buffer, ListBuffer}

  case class Meeting(start: Long, end: Long, weight: Int) extends Ordered[Meeting] {
    def isOvelappedBy(meeting: Meeting) = {
      val (min, max) = if (this <= meeting) (this, meeting) else (meeting, this)
      (min.end > max.start && min.end < max.end) || (min.end >= max.end)
    }

    override def compare(that: Meeting) = Meeting.ordering.compare(this, that)
  }

  object Meeting {
    def parse(sc: java.util.Scanner) = Meeting(sc.next().toLong, sc.next().toLong, sc.nextInt())

    implicit val ordering: Ordering[Meeting] = Ordering.by(_.start)
  }

  def readInput(): List[Meeting] = {

    val sc = new java.util.Scanner(System.in)
    val n = sc.nextInt
    val meetings = collection.mutable.ListBuffer.empty[Meeting]
    for (i <- 0 until n) meetings += Meeting.parse(sc)
    meetings.toList
  }

  def schedule(meetings: List[Meeting]): Int = {
    val sorted = meetings.sorted
    

    def partitionOverlappings(meeting: Meeting, meetings: List[Meeting]): (List[Meeting], List[Meeting]) = {
      (meetings.filter(meeting.isOvelappedBy(_)), meetings.filter(!meeting.isOvelappedBy(_)))
    }

    def scheduleHelper(meetings: List[Meeting], weight: Int): Int = {
      meetings match {
        case meeting::rest => {
          val (overlappings, notOverlappings) = partitionOverlappings(meeting, meetings)
          math.max(
            scheduleHelper(notOverlappings, weight + meeting.weight),
            overlappings.map(o => scheduleHelper(rest, weight)).max
            )
        }

        case _ => weight
      }
    }

    scheduleHelper(meetings, 0)

  }

  def main(args: Array[String]) {
    val meetings = readInput
    println(schedule(meetings))
  }
}
