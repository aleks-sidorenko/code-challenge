module Main where

import qualified Day01
import qualified Day02
-- import qualified Day03
-- ... import more days as you implement them

-- ============================================
-- SWITCH DAYS HERE - Just change this number!
-- ============================================
currentDay :: Int
currentDay = 2

main :: IO ()
main = case currentDay of
  1 -> Day01.solve
  2 -> Day02.solve
  -- 3 -> Day03.solve
  -- ... add more cases as you implement them
  _ -> putStrLn $ "Day " ++ show currentDay ++ " not implemented yet!"
