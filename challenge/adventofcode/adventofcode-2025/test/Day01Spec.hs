module Day01Spec (spec) where

import Test.Hspec
import Day01

spec :: Spec
spec = do
  describe "rotate" $ do
    context "when rotating right" $ do
      it "wraps around once (50 + R120 -> (70, 1))" $
        rotate 50 (RotateRight 120) `shouldBe` (70, 1)
      
      it "does not wrap (10 + R20 -> (30, 0))" $
        rotate 10 (RotateRight 20) `shouldBe` (30, 0)
      
      it "wraps around multiple times (50 + R250 -> (0, 3))" $
        rotate 50 (RotateRight 250) `shouldBe` (0, 3)

      it "wraps around multiple times (50 + R1000 -> (50, 10))" $
        rotate 50 (RotateRight 1000) `shouldBe` (50, 10)
      
      it "wraps once near boundary (90 + R15 -> (5, 1))" $
        rotate 90 (RotateRight 15) `shouldBe` (5, 1)
      
      it "wraps exactly at boundary (0 + R100 -> (0, 1))" $
        rotate 0 (RotateRight 100) `shouldBe` (0, 1)
      
      it "handles zero rotation (0 + R0 -> (0, 1))" $
        rotate 0 (RotateRight 0) `shouldBe` (0, 1)
    
    context "when rotating left" $ do
 
      it "wraps around multiple times (50 + L250 -> (0, 3))" $
        rotate 50 (RotateLeft 250) `shouldBe` (0, 3)
      
      it "does not wrap (50 + L30 -> (20, 0))" $
        rotate 50 (RotateLeft 30) `shouldBe` (20, 0)
      
      it "wraps around once (20 + L120 -> (0, 2))" $
        rotate 20 (RotateLeft 120) `shouldBe` (0, 2)

      it "wraps exactly at boundary (50 + L50 -> (0, 1))" $
        rotate 50 (RotateLeft 50) `shouldBe` (0, 1)  

      it "wraps near the boundary (50 + L51 -> (99, 1))" $
        rotate 50 (RotateLeft 51) `shouldBe` (99, 1)  

      it "doesn't wrap near the boundary (50 + L49 -> (1, 0))" $
        rotate 50 (RotateLeft 49) `shouldBe` (1, 0)  
      
      it "doesn't wrap near the boundary (0 + L1 -> (99, 0))" $
        rotate 0 (RotateLeft 1) `shouldBe` (99, 0)  

      it "wraps exactly at boundary (0 + L100 -> (0, 1))" $
        rotate 0 (RotateLeft 100) `shouldBe` (0, 1)

  describe "parseItem" $ do
    it "parses right rotation 'R120'" $
      parseItem "R120" `shouldBe` RotateRight 120
    
    it "parses left rotation 'L50'" $
      parseItem "L50" `shouldBe` RotateLeft 50
    
    it "parses zero rotation 'R0'" $
      parseItem "R0" `shouldBe` RotateRight 0
    
    it "parses large rotation 'L999'" $
      parseItem "L999" `shouldBe` RotateLeft 999

  describe "parseInput" $ do
    it "parses a list of rotations" $
      parseInput ["R10", "L20", "", "R30"] `shouldBe` 
        [RotateRight 10, RotateLeft 20, RotateRight 30]
    
    it "filters out empty strings" $
      parseInput ["", "R10", "", "L20", ""] `shouldBe`
        [RotateRight 10, RotateLeft 20]
    
    it "handles empty input" $
      parseInput [] `shouldBe` []
    
    it "handles only empty strings" $
      parseInput ["", "", ""] `shouldBe` []

  describe "process" $ do
    it "processes single rotation with wrap" $
      process [RotateRight 120] 50 `shouldBe` 1
    
    it "processes multiple rotations with wraps" $
      -- First: 50 + 120 = 170 -> position 70, count 1
      -- Second: 70 + 50 = 120 -> position 20, count 1
      -- Total count: 2
      process [RotateRight 120, RotateRight 50] 50 `shouldBe` 2
    
    it "processes rotations with no wraps" $
      -- First: 10 + 10 = 20 -> position 20, count 0
      -- Second: 20 + 20 = 40 -> position 40, count 0
      -- Total count: 0
      process [RotateRight 10, RotateRight 20] 10 `shouldBe` 0
    
    it "processes mixed left and right rotations" $
      -- First: 50 + 60 = 110 -> position 10, count 1
      -- Second: 10 - 20 = -10 -> position 90, count 0
      process [RotateRight 60, RotateLeft 20] 50 `shouldBe` 2
    
    it "processes empty rotation list" $
      process [] 50 `shouldBe` 0
    
    it "accumulates multiple wraps correctly" $
      -- Each R100 from position 0 wraps once
      process [RotateRight 100, RotateRight 100, RotateRight 100] 0 `shouldBe` 3
