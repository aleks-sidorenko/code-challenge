object Solution {
    def increasingTriplet(nums: Array[Int]): Boolean = {
        val (res, _, _) = nums.toList.foldLeft((false, Int.MaxValue, Int.MaxValue)) { 
            case ((false, min1, min2), i) => 
                if (i <= min1) (false, i, min2)                
                else if (i <= min2) (false, min1, i)
                else (true, min1, min2)
            case ((true, min1, min2), _) => (true, min1, min2)
        }
        res
    }
}