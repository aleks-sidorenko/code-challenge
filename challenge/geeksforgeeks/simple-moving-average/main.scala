object Solution {
  import scala.collection._
  

  type Data = Double
    type Sum = Data

  type Case = (List[Data], Int)
    

  def readInput(): List[Case] = {
    (List(1.0, 3.0, 5.0, 6.0, 8.0) -> 3)  :: Nil
  }

  def solution(t: Case): List[Double] = {

    val (list, n) = t
  
    val (res, _, _) = list.foldLeft[(List[Data], List[Data], Sum)]((List.empty[Data], List.empty[Data], 0.0)) { case ((res, cur, sum), x) =>
      
      val newSum = sum + x - (if (cur.size < n) 0.0 else cur.headOption.getOrElse(0.0))
      val average = newSum  / n
      val newCur = x :: cur.take(n - 1)

      (average :: res, newCur, newSum)
    } 
    res.reverse
  }

  

  def main(args: Array[String]) = {
    val cases = readInput()
   
    cases.foreach { c =>
      println(s"${solution(c)}")
    }
  }
}
