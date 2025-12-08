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
    describe "largestJoltage" $ do
      it "returns the largest joltage in a battery" $ do
        largestJoltage [8,1,1,1,1,1,1,1,1,1,1,1,1,1,9] `shouldBe` 89
        largestJoltage [2,3,4,2,3,4,2,3,4,2,3,4,2,7,8] `shouldBe` 78
        largestJoltage [8,1,8,1,8,1,9,1,1,1,1,2,1,1,1] `shouldBe` 92

    