

module Day02 
  ( parseInput
  , solvePart1
  , invalidId1
  , invalidId2
  , invalidIds
  , solve

  ) where


import Data.List.Split (splitOn)

type Range = (Int, Int)
type Input = [Range]


-- Parse the input file into your data structure
parseInput :: String -> Input
parseInput content =
  let ranges = splitOn "," content
  in map parseRange ranges
  where
    parseRange range = 
      let [from, to] = splitOn "-" range
      in (read from, read to)

invalidIds :: (Int -> Bool) -> Range -> [Int]
invalidIds f (from, to) = ids where
  ids = [i | i <- [from..to], f i]

sumInvalidIds :: (Int -> Bool) -> Range -> Int
sumInvalidIds f = sum . invalidIds f


invalidId1 :: Int -> Bool
invalidId1 i = concat (replicate 2 pattern) == str
  where
    str = show i
    len = length str
    len_ = len `div` 2
    pattern = take len_ str    

invalidId2 :: Int -> Bool
invalidId2 i = any canSplit [1..end]
  where
    str = show i
    len = length str
    end = len `div` 2    
    canSplit patternLen = 
      let pattern = take patternLen str
          repetitions = len `div` patternLen
      in repetitions >= 2 && concat (replicate repetitions pattern) == str


-- Part 1 solution
solvePart1 :: Input -> Int
solvePart1 = sum . map (sumInvalidIds invalidId1)

solvePart2 :: Input -> Int
solvePart2 = sum . map (sumInvalidIds invalidId2)


-- Main solve function - reads input and prints results
solve :: IO ()
solve = do
  content <- readFile "input/day02.txt"
  let input = parseInput content
  putStrLn $ "Solutions:"
  putStrLn $ "Part 1: " ++ show (solvePart1 input)
  putStrLn $ "Part 2: " ++ show (solvePart2 input)
