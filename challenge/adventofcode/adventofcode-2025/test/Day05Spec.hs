module Day05Spec (spec) where

import Test.Hspec
import Day05

spec :: Spec
spec = do
  describe "parseInput" $ do
    it "parses ranges and ids from the example input" $ do
      let input = "3-5\n10-14\n16-20\n12-18\n\n1\n5\n8\n11\n17\n32\n"
      let expected = ([(3,5), (10,14), (16,20), (12,18)], [1, 5, 8, 11, 17, 32])
      parseInput input `shouldBe` expected
    
    it "parses ranges only (no ids)" $ do
      let input = "1-5\n10-20\n\n"
      let expected = ([(1,5), (10,20)], [])
      parseInput input `shouldBe` expected
    
    it "parses a single range and single id" $ do
      let input = "5-10\n\n15\n"
      let expected = ([(5,10)], [15])
      parseInput input `shouldBe` expected
    
    it "handles multiple ranges with various sizes" $ do
      let input = "1-2\n100-200\n5-7\n\n10\n20\n30\n"
      let expected = ([(1,2), (100,200), (5,7)], [10, 20, 30])
      parseInput input `shouldBe` expected
  
  describe "solvePart1" $ do
    it "solves the example input" $ do
      let content = "3-5\n10-14\n16-20\n12-18\n\n1\n5\n8\n11\n17\n32\n"
      let input = parseInput content
      solvePart1 input `shouldBe` 3
  
  describe "solvePart2" $ do
    it "solves the example input" $ do
      let content = "3-5\n10-14\n16-20\n12-18\n\n1\n5\n8\n11\n17\n32\n"
      let input = parseInput content
      solvePart2 input `shouldBe` 14

