module Day03Spec (spec) where

import Test.Hspec
import Day03

spec :: Spec
spec = do
  describe "Day03" $ do
    describe "parseInput" $ do
      it "parses example input correctly" $ do
        let input = "811111111111119\n234234234234278\n818181911112111"
        let expected = [[8,1,1,1,1,1,1,1,1,1,1,1,1,1,9], [2,3,4,2,3,4,2,3,4,2,3,4,2,7,8], [8,1,8,1,8,1,9,1,1,1,1,2,1,1,1]]
        parseInput input `shouldBe` expected
    describe "largestJoltage1" $ do
      it "returns the largest joltage in a battery" $ do
        largestJoltage1 [8,1,1,1,1,1,1,1,1,1,1,1,1,1,9] `shouldBe` 89
        largestJoltage1 [2,3,4,2,3,4,2,3,4,2,3,4,2,7,8] `shouldBe` 78
        largestJoltage1 [8,1,8,1,8,1,9,1,1,1,1,2,1,1,1] `shouldBe` 92

    describe "largestJoltage2" $ do
      it "returns the largest joltage in a battery" $ do
        largestJoltage2 12 [9,8,7,6,5,4,3,2,1,1,1,1,1,1,1,1] `shouldBe` 987654321111
        largestJoltage2 12 [8,1,1,1,1,1,1,1,1,1,1,1,1,1,9] `shouldBe` 811111111119
        largestJoltage2 12 [2,3,4,2,3,4,2,3,4,2,3,4,2,7,8] `shouldBe` 434234234278
        largestJoltage2 12 [8,1,8,1,8,1,9,1,1,1,1,2,1,1,1] `shouldBe` 888911112111

    