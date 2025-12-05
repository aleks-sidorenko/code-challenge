-- Example template for Day 2 tests
-- Rename this file to Day02Spec.hs to use it

module Day02Spec (spec) where

import Test.Hspec
import Day02

spec :: Spec
spec = do
  describe "Day02" $ do
    describe "parseInput" $ do
      it "parses example input correctly" $ do
        let input = "7 6 4 2 1\n1 2 7 8 9\n9 7 6 2 1"
        let expected = [[7,6,4,2,1], [1,2,7,8,9], [9,7,6,2,1]]
        parseInput input `shouldBe` expected

    describe "part1" $ do
      it "solves the example from the problem" $ do
        let input = [[7,6,4,2,1], [1,2,7,8,9], [9,7,6,2,1], 
                     [1,3,2,4,5], [8,6,4,4,1], [1,3,6,7,9]]
        part1 input `shouldBe` 2  -- Update with expected result

      it "handles empty input" $ do
        part1 [] `shouldBe` 0

    describe "part2" $ do
      it "solves the example from the problem" $ do
        let input = [[7,6,4,2,1], [1,2,7,8,9], [9,7,6,2,1], 
                     [1,3,2,4,5], [8,6,4,4,1], [1,3,6,7,9]]
        part2 input `shouldBe` 4  -- Update with expected result

      it "handles empty input" $ do
        part2 [] `shouldBe` 0

