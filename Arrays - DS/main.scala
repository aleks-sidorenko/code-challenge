object Solution {

  def reverse(array: Array[Int]) : Array[Int]  = {
    val n: Int = array.length
    val res = new Array[Int](n)
    for(i <- 0 to n-1) {
      res(n-1-i) = array(i)
    }
    res
  }

  def main(args: Array[String]) {
    val sc = new java.util.Scanner (System.in)
    var n = sc.nextInt()
    var arr = new Array[Int](n)
    for(i <- 0 to n-1) {
      arr(i) = sc.nextInt()
    }

    println(reverse(arr).mkString(" "))
  }
}
