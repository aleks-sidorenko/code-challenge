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


invalidIds :: Range -> Int
invalidIds (from, to) = length ids where
  ids = [i | i <- [from..to], invalidId i]

invalidId :: Int -> Bool
invalidId i = any canSplit [1..len `div` 2]
  where
    str = show i
    len = length str
    canSplit patternLen = 
      let pattern = take patternLen str
          repetitions = len `div` patternLen
      in repetitions >= 2 && concat (replicate repetitions pattern) == str


-- Part 1 solution
solvePart1 :: Input -> Int
solvePart1 = foldl (\acc range -> acc + invalidIds range) 0


-- Main solve function - reads input and prints results
solve :: IO ()
solve = do
  content <- readFile "input/day02.txt"
  let input = parseInput content
  putStrLn $ "Day 2 Solutions:"
  putStrLn $ "Part 1: " ++ show (solvePart1 input)

