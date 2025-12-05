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
        let input = "421-455,1278-1279,97621-97622,13245-13246,86441-86442,13679-13680"
        let expected = [(421,455), (1278,1279), (97621,97622), (13245,13246), (86441,86442), (13679,13680)]
        parseInput input `shouldBe` expected

    describe "invalidId" $ do
      it "returns True for 22 (pattern '2' repeated 2 times)" $ do
        invalidId 22 `shouldBe` True

      it "returns True for 1212 (pattern '12' repeated 2 times)" $ do
        invalidId 1212 `shouldBe` True

      it "returns True for 123123 (pattern '123' repeated 2 times)" $ do
        invalidId 123123 `shouldBe` True

      it "returns False for 111 (odd number of digits)" $ do
        invalidId 111 `shouldBe` False

      it "returns False for 55555 (odd number of digits)" $ do
        invalidId 55555 `shouldBe` False

      it "returns False for 123 (odd number of digits)" $ do
        invalidId 123 `shouldBe` False

      it "returns False for single digit 5" $ do
        invalidId 5 `shouldBe` False

      it "returns False for 1213 (not exact repetition)" $ do
        invalidId 1213 `shouldBe` False

      it "returns True for 121212 (pattern '121' doesn't repeat)" $ do
        invalidId 121212 `shouldBe` False

      it "returns False for 100 (no valid pattern)" $ do
        invalidId 100 `shouldBe` False

    describe "invalidIds" $ do
      it "counts invalid IDs in range (10, 30)" $ do
        -- In this range: 11, 22 are invalid
        invalidIds (10, 30) `shouldBe` [11, 22]

      it "counts invalid IDs in range (1, 100)" $ do
        -- Invalid: 11, 22, 33, 44, 55, 66, 77, 88, 99
        invalidIds (1, 100) `shouldBe` [11, 22, 33, 44, 55, 66, 77, 88, 99] 

      it "counts invalid IDs in range (100, 200)" $ do
        -- Invalid: 101101, 111, 121121, etc.
        -- Let's calculate: 111 and potentially others
        invalidIds (100, 200) `shouldBe` []

      it "returns 0 for range with no invalid IDs (101, 110)" $ do
        invalidIds (101, 110) `shouldBe` []

      it "counts invalid IDs in range (1200, 1300)" $ do
        -- Invalid: 1212
        invalidIds (1200, 1300) `shouldBe` [1212] 

      it "counts invalid IDs in small range (20, 25)" $ do
        -- Invalid: 22
        invalidIds (20, 25) `shouldBe` [22]

      it "returns 0 for single element range with valid ID (123, 123)" $ do
        invalidIds (123, 123) `shouldBe` []

      it "returns 1 for single element range with invalid ID (22, 22)" $ do
        invalidIds (22, 22) `shouldBe` [22]

      it "returns 2 for range with invalid IDs (11, 22)" $ do
        invalidIds (11, 22) `shouldBe` [11, 22]

      it "counts invalid IDs in range (1, 1000)" $ do
        -- Invalid: 11,22,33,44,55,66,77,88,99 (9)
        --         111,222,333,444,555,666,777,888,999 (9)
        -- Total: 18
        invalidIds (1, 1000) `shouldBe` [11, 22, 33, 44, 55, 66, 77, 88, 99]

      it "counts invalid IDs in range (95, 115) - has one invalid ID, 99" $ do
        invalidIds (95, 115) `shouldBe` [99]

      it "counts invalid IDs in range (998, 1012) - has one invalid ID, 1010" $ do
        invalidIds (998, 1012) `shouldBe` [1010]

      it "counts invalid IDs in range (1188511880, 1188511890) - has one invalid ID, 1188511885" $ do
        invalidIds (1188511880, 1188511890) `shouldBe` [1188511885]

      it "counts invalid IDs in range (222220, 222224) - has one invalid ID, 222222" $ do
        invalidIds (222220, 222224) `shouldBe` [222222] 

      it "counts invalid IDs in range (1698522, 1698528) - contains no invalid IDs" $ do
        invalidIds (1698522, 1698528) `shouldBe` []

      it "counts invalid IDs in range (446443, 446449) - has one invalid ID, 446446" $ do
        invalidIds (446443, 446449) `shouldBe` [446446]

      it "counts invalid IDs in range (38593856, 38593862) - has one invalid ID, 38593859" $ do
        invalidIds (38593856, 38593862) `shouldBe` [38593859]

      it "counts invalid IDs in range (2121212118, 2121212124) - contains no invalid IDs" $ do
        invalidIds (2121212118, 2121212124) `shouldBe` []


    describe "solvePart1" $ do
      it "should return 1227775554 for input from test.txt (lines 1-3)" $ do
        let input = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124"
        let parsed = parseInput input
        solvePart1 parsed `shouldBe` 1227775554
