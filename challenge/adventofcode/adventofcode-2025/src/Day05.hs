
module Day05
  ( parseInput
  , solvePart1
  , solvePart2
  , solve 
  
  ) where

import Data.List (sortBy)
import Data.List.Split (splitOn)

type Id = Int
type Range = (Int, Int)
type Input = ([Range], [Id])

inRange :: Id -> Range -> Bool
inRange id (start, end) = id >= start && id <= end

overlap :: Range -> Range -> Bool
overlap (start1, end1) (start2, end2) = start1 <= end2 && end1 >= start2 || start2 <= end1 && end2 >= start1

merge :: Range -> Range -> Range
merge (start1, end1) (start2, end2) = (min start1 start2, max end1 end2)

ids :: Range -> Int
ids (start, end) = end - start + 1

-- Parse the input file into your data structure
parseInput :: String -> Input
parseInput content = (ranges, ids)
  where
    sections = splitOn "\n\n" content
    rangeLines = lines (head sections)
    idLines = if length sections > 1 
              then filter (not . null) (lines (sections !! 1))
              else []
    
    ranges = map parseRange rangeLines
    ids = map read idLines
    
    parseRange :: String -> Range
    parseRange line = 
      let [start, end] = splitOn "-" line
      in (read start, read end)
    
-- brute force solution, let's see how it performs
solvePart1 :: Input -> Int
solvePart1 (ranges, ids) = length $ filter (inRanges ranges) ids
  where
    inRanges :: [Range] -> Id -> Bool
    inRanges ranges id = any (inRange id) ranges
   
solvePart2 :: Input -> Int
solvePart2 (ranges, _) = sum $ map ids (foldl mergeRanges [] sortedRanges)
  where    
    sortedRanges = sortBy (\(start1, _) (start2, _) -> compare start2 start1) ranges -- sort by start descending    
    mergeRanges :: [Range] -> Range -> [Range]
    mergeRanges [] range = [range]
    mergeRanges (x:xs) range = if overlap x range then merge x range : xs else range : x:xs

-- Main solve function - reads input and prints results
solve :: IO ()
solve = do
  content <- readFile "input/day05.txt"
  let input = parseInput content
  putStrLn $ "Solutions:"
  putStrLn $ "Part 1: " ++ show (solvePart1 input)
  putStrLn $ "Part 2: " ++ show (solvePart2 input)
