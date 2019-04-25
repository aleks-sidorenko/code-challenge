object Solution {
    type Pattern = List[List[Int]]
    def findAndReplacePattern(words: Array[String], pat: String): List[String] = {
        def pattern(s: String): Pattern = {
            val map: Map[Char, List[Int]] = s.toList.zipWithIndex.groupBy({ case (x, _) => x })
                .map { case (k, v) => (k, v.map(_._2)) }
            
            s.toList.map(map(_))
        }
        
        words.filter(w => pattern(pat) == pattern(w)).toList
    }
}