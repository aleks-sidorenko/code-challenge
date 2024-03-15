pub fn can_jump(nums: Vec<i32>) -> bool {
    let (mut acc, mut i) = (nums[0], 0);

    return loop {
        let last = nums.len() - 1;
        acc = cmp::max(acc - 1, nums[i]);
        if acc == 0 && i != last {
            break false;
        } else if i == last {
            break true;
        } else {
            i += 1;
        }
    };
}
