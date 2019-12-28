
-- Complete the climbingLeaderboard function below.


type Score = Int
type Rank = Int

maxScore = maxBound :: Score

scoresWithRank :: [Score] -> [(Score, Rank)]
scoresWithRank scores = (reverse . fst) (foldl f ([], (maxScore, 0)) scores)
    where
        f (acc, (score, rank)) x = ((x, rnk) : acc, (x, rnk))
            where rnk = r score rank x
        r score rank cur = if score > cur then rank + 1 else rank

        
climbingLeaderboard :: [Score] [Score] = do
climbingLeaderboard scores alice = undefined
    where
        f 
        scs = scoresWithRank scores
