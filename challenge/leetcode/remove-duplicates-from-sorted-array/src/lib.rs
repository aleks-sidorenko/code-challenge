pub fn remove_duplicates(nums: &mut Vec<i32>) -> i32 {
    let mut rm = 0;
    let (mut j, mut val) = (0, nums[0]);
    for i in 0..nums.len() {
        if nums[i] != val {
            j = i - rm + 1;
            val = nums[i];
            nums[j] = val;
        } else {
            rm += 1;
        }
    }
    return (nums.len() - rm + 1) as i32;
}
