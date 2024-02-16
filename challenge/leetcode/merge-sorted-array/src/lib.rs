pub fn merge(res: &mut Vec<i32>, m: i32, nums2: &mut Vec<i32>, n: i32) {
    let nums1: Vec<i32> = Vec::from_iter(res[..m as usize].iter().cloned());

    let (mut iter1, mut iter2) = (nums1.iter(), nums2.iter());
    let (mut x, mut y) = (iter1.next(), iter2.next());
    let mut i = 0;
    loop {
        match (x, y) {
            (Some(a), Some(b)) => {
                if a >= b {
                    res[i] = *b;
                    y = iter2.next();
                } else {
                    res[i] = *a;
                    x = iter1.next();
                }
            }
            (Some(a), None) => {
                res[i] = *a;
                x = iter1.next();
            }
            (None, Some(b)) => {
                res[i] = *b;
                y = iter2.next();
            }
            _ => break,
        }
        i += 1;
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_example1() {
        let (mut nums1, m, mut num2, n) = (vec![1, 2, 3, 0, 0, 0], 3, vec![2, 5, 6], 3);
        let expected = vec![1, 2, 2, 3, 5, 6];

        merge(&mut nums1, m, &mut num2, n);

        assert_eq!(nums1, expected);
    }
}
