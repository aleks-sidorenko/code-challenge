object Solution {
  import scala.collection._
  
  type Case = (List[Int], List[Int])

  def input(): List[Case] = {
    (2 :: 6  :: 4 :: Nil, 3 :: 6 :: 7 :: 9 :: Nil) ::
    (2 :: 1  :: -7 :: Nil, 3 :: 6 :: 7 :: 9 :: Nil) :: 
    Nil
  }

  def solve(cs: Case): Option[Int] = {
    val (x, y) = cs

    val common = x.toSet & y.toSet

    if (common.nonEmpty) Some(common.min) else None

  }

  
  def main(args: Array[String]) = {
    val cases = input() 
    cases.foreach { c =>
      println(s"${solve(c)}")
    }
  }
}
