
module Day03
  ( parseInput
  , solvePart1
  , solvePart2
  , solve
  , largestJoltage1
  , largestJoltage2
  ) where

type Digit = Int
type Joltage = Int
type Battery = [Joltage]
type Input = [Battery]

-- Parse the input file into your data structure
parseInput :: String -> Input
parseInput = map parseBattery . lines where
  parseJoltage :: Char -> Joltage
  parseJoltage = read . (: [])
  parseBattery :: String -> Battery
  parseBattery = map parseJoltage
    

largestJoltage1 :: Battery -> Joltage
largestJoltage1 = largestJoltage2 2

largestJoltage2 :: Int -> Battery -> Joltage
largestJoltage2 n xs = digitsToJoltage $ reverse $ loop n xs []
  where
    loop :: Int -> Battery -> [Digit] -> [Digit]
    loop n xs result =
      if n == 0 || null xs then result
      else  loop (n - 1) xs_ result_
      where
        max = maximum (take (length xs - n + 1) xs)
        maxIndex = firstIndexOf max xs
        result_ = max : result
        xs_ = drop (maxIndex + 1) xs
        firstIndexOf :: Eq a => a -> [a] -> Int
        firstIndexOf x xs = 
          let indices = [i | (i, v) <- zip [0..] xs, v == x]
          in head indices
    digitsToJoltage :: [Digit] -> Joltage
    digitsToJoltage = foldl (\acc x -> acc * 10 + x) 0
    
    
solvePart1 :: Input -> Int
solvePart1 = sum . map largestJoltage1

solvePart2 :: Input -> Int
solvePart2 = sum . map (largestJoltage2 12)


-- Main solve function - reads input and prints results
solve :: IO ()
solve = do
  content <- readFile "input/day03.txt"
  let input = parseInput content
  putStrLn $ "Solutions:"
  putStrLn $ "Part 1: " ++ show (solvePart1 input)
  putStrLn $ "Part 2: " ++ show (solvePart2 input)
