object Solution {

    def readInput(): (String, String) = {
        val sc = new java.util.Scanner (System.in);
        var str1 = sc.nextLine();
        var str2 = sc.nextLine();        
        (str1, str2)
    }
    
    type Frequence = Map[Char, Int]
    
    implicit class StringOps(str: String) {
        
        def toFrequence(): Frequence = {
            str.groupBy(identity).map { case (c, cs) => c -> cs.length }
        }        
    }
    
        
    def common(f1: Frequence, f2: Frequence): Frequence = {  
        val commonChars = (f1.keySet & f2.keySet).toList
        commonChars.map { x => x -> Math.min(f1.get(x).getOrElse(0), f2.get(x).getOrElse(0)) }
        .toMap
    }
    
    def diff(str1: String, str2: String): Int = {
        
        val frequence1 = str1 toFrequence
        val frequence2 = str2 toFrequence
        
        val d = common(frequence1,  frequence2)
        
        str1.length  + str2.length - 2 * d.values.sum
    }
    
    def main(args: Array[String]) {
        val (str1, str2) = readInput();
        
        println(diff(str1, str2))
        
    }
}
