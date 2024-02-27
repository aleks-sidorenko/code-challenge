pub fn rotate(nums: &mut Vec<i32>, k: i32) {
    let mut count = 0;
    let (mut i, mut f) = (0, 0);
    let mut n = nums[i];

    while (count < nums.len()) {
        if f == i && count > 0 {
            f += 1;
            i = f;
            n = nums[i];
        }

        let ni = (i + k as usize) % nums.len();

        let n1 = nums[ni];
        nums[ni] = n;
        n = n1;

        i = ni;
        count += 1;
    }

    return ();
}
