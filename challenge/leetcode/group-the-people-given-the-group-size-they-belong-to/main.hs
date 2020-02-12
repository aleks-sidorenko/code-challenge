import Data.List
import qualified Data.Map as Map
import Control.Arrow

type Id = Int
type Size = Int
type Groups = [(Size,[Id])]


zipWithIndex :: [Size] -> [(Size,Id)]
zipWithIndex sizes = zip sizes [0..]

mapValues :: [(Size,Id)] -> Groups
mapValues l = map (\(g,i) -> (g,[i])) l

histogram :: Groups -> Groups
histogram = Map.toList . (Map.fromListWith (++))


split :: Groups -> [[Id]]
split = loop [] where
  loop acc [] = acc
  loop acc ((g,ids):xs) =
   if g == length ids 
   then loop ((take g ids):acc) xs 
   else loop ((take g ids):acc) ((g,drop g ids):xs)


groupThePeople :: [Size] -> [[Id]]
groupThePeople = split . histogram . mapValues . zipWithIndex
