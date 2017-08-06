import scala.collection.mutable.ListBuffer


class CycleDetector[T] (val seq: Iterable[T]){
  def getCycle(): Iterable[T] = {
    val searched = ListBuffer[T]()
    var cycle : List[T] = null
    seq.takeWhile(x => {
      val b = searched.lastIndexOf(x)
      if (b != -1) {
        cycle = searched.slice(b, searched.length).toList
      }
      searched.append(x)
      b == -1
    })

    cycle

  }
}


object Main extends App {
  val source = scala.io.Source.fromFile(args(0))
  val lines = source.getLines.filter(_.length > 0)
  for (line <- lines) {
    if (!line.isEmpty){
      val detector = new CycleDetector[Int](line.split(' ').map(_.toInt))
      println(detector.getCycle().mkString(" "))

    }
  }
}
