object Solution {
    def sortArray(nums: Array[Int]): Array[Int] = {

        val cache = nums.groupBy(identity).mapValues(_.length)
        
        (-50000 to 50000).filter(cache.contains(_)).flatMap { x =>  List.fill(cache(x))(x) } toArray
    }
}