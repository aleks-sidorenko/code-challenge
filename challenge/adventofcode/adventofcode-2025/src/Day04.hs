
module Day04
  ( parseInput
  , solvePart1
  , solvePart2
  , solve 
  , Cell(..)
  ) where

type Grid = [[Cell]]
data Cell = Paper | Empty deriving (Show, Eq)
type Coord = (Int, Int)
type Dimensions = (Int, Int)
type Input = Grid

-- Get cell at coordinate (x, y), throws error if out of bounds
getCell :: Grid -> Coord -> Cell
getCell grid (x, y) = grid !! y !! x

-- Get all coordinates in the grid
coords :: Grid -> [Coord]
coords grid = [(x, y) | x <- [0..width-1], y <- [0..height-1]]
  where (width, height) = dimensions grid

-- Get grid dimensions (width, height)
-- Fold over the grid, starting with the initial value and applying the function to each cell
foldGrid :: Grid -> t -> (t -> Coord -> t) -> t
foldGrid grid initial f = foldl f initial (coords grid)

mapGrid :: Grid -> (Coord -> Cell -> Cell) -> Grid
mapGrid grid f = map mapRow (zip [0..] grid)
  where    
    mapRow :: (Int, [Cell]) -> [Cell]
    mapRow (y, row) = map (\(x, cell) -> f (x, y) cell) (zip [0..] row)


dimensions :: Grid -> Dimensions
dimensions grid = (length (head grid), length grid)

-- Check if coordinates are within bounds
inBounds :: Grid -> Coord -> Bool
inBounds grid (x, y) = x >= 0 && x < width && y >= 0 && y < height
  where (width, height) = dimensions grid

-- Get all neighbors (8-directional: including diagonals)
neighbors8 :: Grid -> Coord -> [Coord]
neighbors8 grid (x, y) = 
  filter (inBounds grid) 
    [ (x+1, y), (x-1, y), (x, y+1), (x, y-1)
    , (x+1, y+1), (x+1, y-1), (x-1, y+1), (x-1, y-1)
    ]

neighbors8Cells :: Grid -> Coord -> [Cell]
neighbors8Cells grid (x, y) = map (getCell grid) (neighbors8 grid (x, y))

isAccessible :: Grid -> Coord -> Bool
isAccessible grid coord = paper && neighbors < 4
   where
    paper :: Bool
    paper = case (getCell grid coord) of
      Paper -> True
      Empty -> False

    neighbors = sum $ map cell2Int (neighbors8Cells grid coord)
    cell2Int :: Cell -> Int
    cell2Int Paper = 1
    cell2Int Empty = 0


accessibleCells :: Grid -> [Coord]
accessibleCells grid = filter (isAccessible grid) (coords grid)



-- Parse the input file into your data structure
parseInput :: String -> Input
parseInput content = map parseLine (filter (not . null) (lines content))
  where
    parseLine :: String -> [Cell]
    parseLine = map parseCell
    parseCell :: Char -> Cell
    parseCell '@' = Paper
    parseCell '.' = Empty
    parseCell _ = error "Invalid cell"
    
    
solvePart1 :: Input -> Int
solvePart1 = length . accessibleCells
   
solvePart2 :: Input -> Int
solvePart2 grid = loop grid 0
  where
    loop :: Grid -> Int -> Int
    loop grid acc = if cur == 0 then acc else loop newGrid (acc + cur)
      where
        cells = accessibleCells grid
        cur = length cells
        newGrid = mapGrid grid (\coord cell -> if isAccessible grid coord then Empty else cell)
      


-- Main solve function - reads input and prints results
solve :: IO ()
solve = do
  content <- readFile "input/day04.txt"
  let input = parseInput content
  putStrLn $ "Solutions:"
  putStrLn $ "Part 1: " ++ show (solvePart1 input)
  putStrLn $ "Part 2: " ++ show (solvePart2 input)
