pub fn majority_element(nums: Vec<i32>) -> i32 {
    if nums.len() == 1 {
        return nums[0];
    }
    
    let (mut n1, mut c1) = (nums[0], 0);
    let (mut n2, mut c2) = (nums[1], 0);
    
    for n in nums {
        
        if n == n1 {
            c1 += 1;
        } else if n == n2 {
            c2 += 1;
        } else if c1 <= c2 {
            n1 = n;
            c1 = 1;
        } else {
            n2 = n;
            c2 = 1;
        }
    }
    return if (c1 >= c2) { n1 } else { n2 };
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn it_works() {
        let array = vec![3,2,3];
        let result = majority_element(array);
        assert_eq!(result, 3);
    }
}
