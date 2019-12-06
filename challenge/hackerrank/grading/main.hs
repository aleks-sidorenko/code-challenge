
--
-- Complete the 'gradingStudents' function below.
--
-- The function is expected to return an INTEGER_ARRAY.
-- The function accepts INTEGER_ARRAY grades as parameter.
--
gradingStudents :: [Grade] -> [Grade]
gradingStudents = List.map calcGrade
    -- Write your code here

type Grade = Int

calcGrade :: Grade -> Grade
calcGrade grade
            | grade < 38 = grade
            | next - grade < 3 = next
            | otherwise = grade
            where 
                diff = 5 -  grade `mod` 5
                next = grade + diff
                