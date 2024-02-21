pub fn remove_element(nums: &mut Vec<i32>, val: i32) -> i32 {
    let mut j = 0;
    for i in 0..nums.len() {
        if nums[i] == val {
            j += 1;
        } else {
            if j > 0 {
                nums[i - j] = nums[i];
            }
        }
    }
    return (nums.len() - j) as i32;
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn it_works() {
        let result = remove_element(&mut vec![3, 2, 2, 3], 3);
        assert_eq!(result, 2);
    }
}
