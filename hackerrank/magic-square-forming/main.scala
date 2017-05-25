object Solution {
  final val n = 3

  object Matrix {
    def diff(m1: Matrix, m2: Matrix): Int = {
      0
    }
  }

  case class Matrix(m: Array[Array[Int]]) {
    def mkString() = {
      m.map(_.mkString(" ")).mkString("\n")
    }
  }

  def readInput(): Matrix = {
      val sc = new java.util.Scanner (System.in);
      var s = Array.ofDim[Int](n,n);
      for(i <- 0 until n) {
          for(j <- 0 until n){
            s(i)(j) = sc.nextInt();
          }
      }
      Matrix(s)
  }

  
  
  def main(args: Array[String]) {
    val matrix = readInput()
    println(matrix.mkString())
  }
}
