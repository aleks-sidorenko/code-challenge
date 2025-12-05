module Day01 
  ( Count
  , Position
  , Rotate(..)
  , parseItem
  , parseInput
  , rotate
  , process
  , solve
  ) where

type Count = Int
type Position = Int
data Rotate = RotateLeft Int | RotateRight Int deriving (Show, Eq)


-- Parse function that takes a string and returns a Rotate
parseItem :: String -> Rotate
parseItem ('L':n) = RotateLeft (read n)
parseItem ('R':n) = RotateRight (read n)
parseItem _ = error "Invalid rotate"

-- Parse input function that takes a list of strings and returns a list of Rotates
parseInput :: [String] -> [Rotate]
parseInput = map parseItem . filter (\s -> (startWith s "R" || startWith s "L"))
  where
    startWith s prefix = take (length prefix) s == prefix


-- Rotate function that takes a position and a rotation
-- Returns new position (0-99) and count of how many times we passed through 0
rotate :: Position -> Rotate -> (Position, Count)
rotate position rt = (np, nc) where
  n = case rt of
    RotateLeft n -> -n
    RotateRight n -> n

  rp = position + n  -- -99
  np = rp `mod` 100 -- -99 mode 100 = 1
  nc_ = abs (rp `quot` 100)
  nc = if rp < 0 then nc_ + (if position /= 0 then 1 else 0) else if rp == 0 then 1 else nc_


-- Process function that takes a list of rotations and starting position
-- Returns the total count of zero crossings
process :: [Rotate] -> Position -> Int
process rotates startPosition = cnt where
  (_, cnt) = foldl rotate2 (startPosition, 0) rotates
  rotate2 (p, c) r = 
    let (np, nc) = rotate p r        
    in (np, c + nc)

solve :: IO ()
solve = do
  content <- readFile "input/day01.txt"
  let input = parseInput (lines content)
  let result = process input 50
  putStrLn $ "Result: " ++ show result
