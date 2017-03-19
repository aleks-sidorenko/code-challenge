object Solution {

    def readInput(): List[Int] = {
        val sc = new java.util.Scanner (System.in);
        val n = sc.nextInt();
        val array = new Array[Int](n)
        for (i <- 0 until n) {
            array(i) = sc.nextInt()
        }
        array.toList
    }
    
    def getMaxFreq(array: List[Int]): Int = {
        array.length - array.groupBy(identity).mapValues(_.length).maxBy(el => el._2)._2
    }
        
    def main(args: Array[String]) {
        val array = readInput()
        println(getMaxFreq(array))
        
    }
}