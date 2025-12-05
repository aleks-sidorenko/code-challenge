-- Example template for Day 2
-- Rename this file to Day02.hs to use it

module Day02 
  ( parseInput
  , part1
  , part2
  , solve
  ) where

-- Define your types based on the problem
type Report = [Int]
type Input = [Report]

-- Parse the input file into your data structure
parseInput :: String -> Input
parseInput content = map parseLine (lines content)
  where
    parseLine line = map read (words line)

-- Part 1 solution
part1 :: Input -> Int
part1 reports = length (filter isSafe reports)
  where
    isSafe report = True  -- TODO: Implement your logic here

-- Part 2 solution
part2 :: Input -> Int
part2 reports = length (filter isSafeWithDampener reports)
  where
    isSafeWithDampener report = True  -- TODO: Implement your logic here

-- Main solve function - reads input and prints results
solve :: IO ()
solve = do
  content <- readFile "input/day02.txt"
  let input = parseInput content
  putStrLn $ "Day 2 Solutions:"
  putStrLn $ "Part 1: " ++ show (part1 input)
  putStrLn $ "Part 2: " ++ show (part2 input)

