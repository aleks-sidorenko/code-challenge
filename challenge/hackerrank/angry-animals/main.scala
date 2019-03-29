object Solution {
  import scala.collection._

  def readInput(): List[(Int, Array[Int], Array[Int])] = {
    (4, Array(1, 2), Array(3, 4)) :: Nil
  }

  def angryAnimals(n: Int, a: Array[Int], b: Array[Int]): Long = {
    
    val animals = (1 to n).toList
    val enemies = (a.toList zip b.toList)
      .flatMap { case (x, y) =>  List(x -> y, y -> x) }
      .groupBy { case (x, _) => x }
      .mapValues { x ⇒ x.map { case (_, y) ⇒ y } }
      
    
    def allGroups(all: List[Int]) =
      (for {
        start <- 0 to all.length
        end   <- start to all.length
      } yield all.drop(start).take(end - start)).filter { _.nonEmpty }

    def hasEnemies(list: List[Int]): Boolean = {
      (list.flatMap { x => enemies.getOrElse(x, Nil) }.toSet intersect list.toSet).size >= 2
    }

    allGroups(animals).filterNot { x => hasEnemies(x) }.size 
  }

  def main(args: Array[String]) = {
    val cases = readInput()
    cases.foreach { case (n, a, b) =>
      println(angryAnimals(n, a, b))
    }
  }
}
