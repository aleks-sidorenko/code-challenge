module Day04Spec (spec) where

import Test.Hspec
import Day04

spec :: Spec
spec = do
  describe "parseInput" $ do
    it "parses a single line with one Paper cell" $ do
      parseInput "@" `shouldBe` [[Paper]]
    
    it "parses a single line with one Empty cell" $ do
      parseInput "." `shouldBe` [[Empty]]
    
    it "parses a single line with multiple cells" $ do
      parseInput "@.@" `shouldBe` [[Paper, Empty, Paper]]
    
    it "parses multiple lines (2x2 grid)" $ do
      parseInput "@.\n.@" `shouldBe` [[Paper, Empty], [Empty, Paper]]
    
    it "parses multiple lines (3x3 grid)" $ do
      let input = "@.@\n.@.\n@.@"
      let expected = [ [Paper, Empty, Paper]
                     , [Empty, Paper, Empty]
                     , [Paper, Empty, Paper]
                     ]
      parseInput input `shouldBe` expected
    
    it "parses the example input (10x10 grid)" $ do
      let input = "..@@.@@@@.\n\
                  \@@@.@.@.@@\n\
                  \@@@@@.@.@@\n\
                  \@.@@@@..@.\n\
                  \@@.@@@@.@@\n\
                  \.@@@@@@@.@\n\
                  \.@.@.@.@@@\n\
                  \@.@@@.@@@@\n\
                  \.@@@@@@@@.\n\
                  \@.@.@@@.@."
      let result = parseInput input
      -- Check dimensions
      length result `shouldBe` 10
      all (\row -> length row == 10) result `shouldBe` True
      -- Check first row
      head result `shouldBe` [Empty, Empty, Paper, Paper, Empty, Paper, Paper, Paper, Paper, Empty]
      -- Check last row
      last result `shouldBe` [Paper, Empty, Paper, Empty, Paper, Paper, Paper, Empty, Paper, Empty]
  
  describe "solvePart1" $ do
    it "solves the example input (should be 13)" $ do
      let input = "..@@.@@@@.\n\
                  \@@@.@.@.@@\n\
                  \@@@@@.@.@@\n\
                  \@.@@@@..@.\n\
                  \@@.@@@@.@@\n\
                  \.@@@@@@@.@\n\
                  \.@.@.@.@@@\n\
                  \@.@@@.@@@@\n\
                  \.@@@@@@@@.\n\
                  \@.@.@@@.@."
      let grid = parseInput input
      print $ show grid
      solvePart1 grid `shouldBe` 13
  
  describe "solvePart2" $ do
    it "solves the example input (should be 43)" $ do
      let input = "..@@.@@@@.\n\
                  \@@@.@.@.@@\n\
                  \@@@@@.@.@@\n\
                  \@.@@@@..@.\n\
                  \@@.@@@@.@@\n\
                  \.@@@@@@@.@\n\
                  \.@.@.@.@@@\n\
                  \@.@@@.@@@@\n\
                  \.@@@@@@@@.\n\
                  \@.@.@@@.@."
      let grid = parseInput input
      solvePart2 grid `shouldBe` 43
   