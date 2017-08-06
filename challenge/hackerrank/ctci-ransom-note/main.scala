object Solution {
    
    def readInput(): (List[String], List[String]) = {
        val sc = new java.util.Scanner (System.in);
        var m = sc.nextInt();
        var n = sc.nextInt();
        var magazine = new Array[String](m);
        for(magazine_i <- 0 to m-1) {
           magazine(magazine_i) = sc.next();
        }
        var ransom = new Array[String](n);
        for(ransom_i <- 0 to n-1) {
           ransom(ransom_i) = sc.next();
        }
        
        (magazine.toList, ransom.toList)
    }
    
    def contains(dictionary: List[String], phrase: List[String]): Boolean = {
        def toFrequency(words: List[String]) = words.groupBy(identity).mapValues(_.length)
        
        val dictFreq = toFrequency(dictionary)
        val phraseFreq = toFrequency(phrase)
        
        phraseFreq
            .map { case (w, c) => dictFreq.get(w).getOrElse(0) - c }
            .forall { _ >= 0 }
    }
    
    def main(args: Array[String]) {
        val (magazine, ransom) = readInput()
         
        print(if (contains(magazine, ransom)) "Yes" else "No")
        
    }
}
