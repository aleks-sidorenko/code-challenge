pub fn longest_common_prefix(strs: Vec<String>) -> String {
    fn lcp(s1: String, s2: String) -> String {
        let common: Vec<char> = s1
            .chars()
            .zip(s2.chars())
            .take_while(|(a, b)| a == b)
            .map(|(a, b)| a)
            .collect();
        return String::from_iter(common);
    }

    strs.into_iter()
        .reduce(|acc, str| lcp(acc, str))
        .unwrap()
        .to_string()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn it_works() {
        let result = longest_common_prefix(vec![
            "flower".to_owned(),
            "flow".to_owned(),
            "fleet".to_owned(),
        ]);
        assert_eq!(result, "fl".to_owned());
    }
}
