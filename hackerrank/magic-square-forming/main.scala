object Solution {
  import scala.collection._

  object Matrix {
    def apply(n: Vector[Int]) = {
      val m = Array.ofDim[Int](3,3)
        for(i <- 0 until 3) {
            for(j <- 0 until 3){
              m(i)(j) = n(i*3 + j)
            }
      }
      new Matrix(m)
    } 

    def diff(m1: Matrix, m2: Matrix): Int = {
      val l1 = m1.m.flatten
      val l2 = m2.m.flatten
      l1.zip(l2).map { case (e1, e2) => math.abs(e1 - e2) }
        .sum
    }

    def magic(): List[Matrix] = {
      val digits = (1 to 9).toList

      digits.permutations.map(p => Matrix(p.toVector)).filter(_.isMagic).toList
    }

  }

  case class Matrix(m: Array[Array[Int]] = Array.fill[Int](3,3)(0)) {
    
    def isMagic(): Boolean = {
      if (m(1)(1) != 5) return false
      val v = Array.fill[Int](3)(0)
      val h = Array.fill[Int](3)(0)
      val d = Array.fill[Int](2)(0)
      for(i <- 0 until 3) {
          for(j <- 0 until 3){
            v(j) += m(i)(j)
            h(i) += m(i)(j)
            if (i == j) d(0) += m(i)(j)
            if (i == 2 - j) d(1) += m(i)(j)
          }
      }
      
      def check(l: Iterable[Int]) = l.forall(_ == 15)
      check(v) && check(h) && check(d)
    }

    def mkString() = {
      m.map(_.mkString(" ")).mkString("\n")
    }
  }

  def readInput(): Matrix = {
      val sc = new java.util.Scanner (System.in)
      var s = Array.ofDim[Int](3,3)
      for(i <- 0 until 3) {
          for(j <- 0 until 3){
            s(i)(j) = sc.nextInt()
          }
      }
      Matrix(s)
  }

  
  
  def main(args: Array[String]) {
    val matrix = readInput()
    val magics = Matrix.magic()
    println(magics.map(m => Matrix.diff(m, matrix)).min)
  }
}
