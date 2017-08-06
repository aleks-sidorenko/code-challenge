object Solution {

  def readInput(): Array[Int] = {
    val sc = new java.util.Scanner(System.in)
    val n = sc.nextInt
    val array = new Array[Int](n)
    for (i <- 0 until n) {
      array(i) = sc.nextInt
    }

    array
  }

  def rearrange(array: Array[Int]): Int = {
    var z = array.length - 1
    var i = 0

    def swap(array: Array[Int], left: Int, right: Int) = {
      if (left < right && array(left) == 0) {
        array(left) = array(right)
        array(right) = 0
      }
    }

    def findRightMostNonZero(array: Array[Int], right: Int): Int = {
      var r = right
      while (r >= 0 && array(r) == 0) r -= 1
      r
    }
    
    while (i <= z) {
      if (array(i) == 0) {
        z = findRightMostNonZero(array, z)
        swap(array, i, z)
      } 
      
      if (i <= z) i += 1
    }
    
    i
  }

  def main(args: Array[String]) {
    val array = readInput()

    println(rearrange(array))
    println(array.mkString(" "))
  }

}
