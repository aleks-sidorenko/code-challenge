

permutations :: [a] -> [[a]]
permutations [] = [[]]
permutations [a] = [[a]]
permutations (x:xs) =  (>>=) (permutations xs)  (merge x)
    where
        insertAt :: Int -> a-> [a] -> [a] 
        insertAt z y xs = as ++ (y:bs)
                  where (as,bs) = splitAt z xs
        merge :: a -> [a] -> [[a]]
        merge x p = map (\i -> insertAt i x p) [0..length p]