-- Example template for Day 2
-- Rename this file to Day02.hs to use it

module Day02 
  ( parseInput
  , solvePart1
  , invalidId
  , invalidIds
  , solve

  ) where


type Range = (Int, Int)
type Input = [Range]

-- Helper function to split a string by a delimiter
split :: Char -> String -> [String]
split _ "" = [""]
split delimiter (c:cs)
  | c == delimiter = "" : rest
  | otherwise = (c : head rest) : tail rest
  where
    rest = split delimiter cs

-- Parse the input file into your data structure
parseInput :: String -> Input
parseInput content =
  let ranges = split ',' content
  in map parseRange ranges
  where
    parseRange range = 
      let [from, to] = split '-' range
      in (read from, read to)

invalidIds :: Range -> [Int]
invalidIds (from, to) = ids where
  ids = [i | i <- [from..to], invalidId i]

sumInvalidIds :: Range -> Int
sumInvalidIds = sum . invalidIds

invalidId :: Int -> Bool
invalidId i = concat (replicate 2 pattern) == str
  where
    str = show i
    len = length str
    len_ = len `div` 2
    pattern = take len_ str    


-- Part 1 solution
solvePart1 :: Input -> Int
solvePart1 = sum . map (sumInvalidIds)


-- Main solve function - reads input and prints results
solve :: IO ()
solve = do
  content <- readFile "input/day02.txt"
  let input = parseInput content
  putStrLn $ "Day 2 Solutions:"
  putStrLn $ "Part 1: " ++ show (solvePart1 input)

