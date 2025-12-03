module Main where

type Position = Int
data Rotate = RotateLeft Int | RotateRight Int deriving (Show, Eq)


-- Parse function that takes a string and returns a Rotate
parseItem :: String -> Rotate
parseItem ('L':n) = RotateLeft (read n)
parseItem ('R':n) = RotateRight (read n)
parseItem _ = error "Invalid rotate"

-- Parse input function that takes a list of strings and returns a list of Rotates
parseInput :: [String] -> [Rotate]
parseInput = map parseItem . filter (/= "")


rotate :: Position -> Rotate -> Position
rotate position (RotateLeft n) = (position - n) `mod` 100
rotate position (RotateRight n) = (position + n) `mod` 100



-- Process function that takes a list of strings
process :: [Rotate] -> Position -> Int
process rotates startPosition= cnt where
  (_, cnt) = foldl rotate2 (startPosition, 0) rotates
  rotate2 (p, c) r = 
    let np = rotate p r
        nc = if np == 0 then c + 1 else c 
    in (np, nc)
    

main :: IO ()
main = do
  content <- readFile "input.txt"
  let input = parseInput (lines content)
  let result = process input 50
  putStrLn $ "Result: " ++ show result
