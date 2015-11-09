object Main extends App {
  val source = scala.io.Source.fromFile(args(0))
  val lines = source.getLines.filter(_.length > 0)
  for (line <- lines) {
    line.find( x => line.indexOf(x) == line.lastIndexOf(x)) match  {
      case Some(x) => println(x)
      case _ => println()
    }

  }
}
