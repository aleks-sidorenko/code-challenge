object Solution {
  
  import scala.collection._

  type Email = String
  
  def readInput(): List[Email] = {
    val sc = new java.util.Scanner (System.in)
    var n = sc.nextInt()
    var emails = new Array[String](n)
    for(i <- 0 until n) {
      emails(i) = sc.next()
    }

    emails.toList
  }

  def isSimilar(email1: Email, email2: Email): Boolean = {
    val maxDistance = 2
    
    def countify(str: String): Map[Char, Int] = {
      str.groupBy(identity).mapValues(_.length)
    }

    def distance(str1: String, str2: String): Int = {
      val (c1, c2) = (countify(str1), countify(str2))
      val (s1, s2) = (c1.keySet, c2.keySet)
      val all = (s1 | s2).toList

      all.map(x => math.abs(c1.getOrElse(x, 0) - c2.getOrElse(x, 0))).sum / 2
    }
    
    distance(email1, email2) <= maxDistance
  }

  def groupBySimilarity(emails: List[Email], similarFunc: (Email, Email) => Boolean): List[Set[Email]] = {
    
    val res = emails.foldLeft(Map.empty[Email, mutable.Set[Email]])( (map, email) => {
      val key = map.keys.filter(k => similarFunc(k, email)).headOption
      if (key.isDefined) {
        map(key.get) += email
        map
      } else {
        val set = new mutable.HashSet[Email]()
        set += email
        map + (email -> set)
      }
    })

    res.mapValues(_.toSet).values.toList
  }
  
  def main(args: Array[String]) {
    val emails = readInput()
    val grouped = groupBySimilarity(emails, isSimilar)
    grouped.foreach(g => println(g.mkString(" ")))
  }

}
