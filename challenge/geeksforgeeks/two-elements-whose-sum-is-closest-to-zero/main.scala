object Solution {
  import scala.collection._
  
  type Case = List[Int]

  def input(): List[Case] = {
    (2 :: 6  :: 4 :: -3 :: 6 :: -7 :: 9 :: Nil) ::
    (2 :: 1  :: -7 :: 3 :: 6 :: 7 :: 9 :: Nil) :: 
    Nil
  }

  def solve(cs: Case): (Int, Int) = {
    val (negative, positive) = cs.sorted.partition { _ < 0 }
    
    // backtracking
    val cache = new collection.mutable.HashMap[(Int, Int), (Int, Int)]()

    assert(cs.length >= 2)

    @inline
    def sum(t: (Int, Int)) = math.abs(t._1 + t._2)
    
    @inline
    def min2(t1: (Int, Int), t2: (Int, Int)): (Int, Int) = if (sum(t1) >= sum(t2)) t2 else t1 
    
    @inline
    def min3(t1: (Int, Int), t2: (Int, Int), t3: (Int, Int)): (Int, Int) = min2(min2(t1, t2), t3)

    @inline
    def key(neg: List[Int], pos: List[Int]) = (neg.length, pos.length)

    def loop(neg: List[Int], pos: List[Int], res: (Int, Int)): (Int, Int) = {
      cache.getOrElseUpdate(key(neg, pos), {
        (neg, pos) match {
          case (n :: ns, p :: ps) => min3(loop(ns, ps, min2(res, n -> p)), loop(ns, pos, res), loop(neg, ps, res))
          case (Nil, p1 :: p2 :: _) => min2(res, p1 -> p2)
          case (n1 :: n2 :: _, Nil) => min2(res, n1 -> n2)
          case _ => res
        }
      })
    }

    loop(negative, positive, Int.MaxValue/2 -> Int.MaxValue)
  }

  
  def main(args: Array[String]) = {
    val cases = input() 
    cases.foreach { c =>
      println(s"${solve(c)}")
    }
  }
}
