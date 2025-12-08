
module Day03
  ( parseInput
  , solvePart1
  , solve
  , largestJoltage
  ) where

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
    

largestJoltage :: Battery -> Joltage
largestJoltage xs = 
  let allExceptLast = init xs
      maxVal = maximum allExceptLast
      maxIndex = firstIndexOf maxVal allExceptLast
      secondPartition = drop (maxIndex + 1) xs
      maxSecond = maximum secondPartition
  in maxVal * 10 + maxSecond
  where
    firstIndexOf :: Eq a => a -> [a] -> Int
    firstIndexOf x xs = 
      let indices = [i | (i, v) <- zip [0..] xs, v == x]
      in head indices 
  
  
solvePart1 :: Input -> Int
solvePart1 = sum . map largestJoltage

solvePart2 :: Input -> Int
solvePart2 = undefined


-- Main solve function - reads input and prints results
solve :: IO ()
solve = do
  content <- readFile "input/day03.txt"
  let input = parseInput content
  putStrLn $ "Day 2 Solutions:"
  putStrLn $ "Part 1: " ++ show (solvePart1 input)
  -- putStrLn $ "Part 2: " ++ show (solvePart2 input)
