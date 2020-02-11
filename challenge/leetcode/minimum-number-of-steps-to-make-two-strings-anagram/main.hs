import qualified Data.Map as Map

type Frequencies = Map.Map Char Int


frequency :: String -> Frequencies
frequency = Map.fromListWith (+) . map (\c -> (c, 1))

diff :: Frequencies -> Frequencies -> Frequencies
diff s t = Map.differenceWithKey f s t where
  f k vs vt = if vs > vt then Just (vs - vt) else Nothing

reduce :: Frequencies -> Int
reduce = Map.foldr (+) 0

minSteps :: String -> String -> Int
minSteps source target = reduce $ diff (frequency source) (frequency target)