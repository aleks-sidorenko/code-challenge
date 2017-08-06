object Solution {

    def readInput(): (List[(Int, Int, Int)]) = {
        val sc = new java.util.Scanner (System.in);
        var n = sc.nextInt()
        var cases = new Array[(Int, Int, Int)](n)
        for(i <- 0 to n-1) {
           cases(i) = (sc.nextInt(), sc.nextInt(), sc.nextInt())
        }
        
        cases.toList
    }
    
    def who(catA: Int, catB: Int, mouseC: Int): String = {
        val (min, max) = (math.min(catA, catB), math.max(catA, catB))

        def out(c: Int) = 
            if (catA == catB) "Mouse C"
            else if (catA == c) "Cat A" 
            else if (c == catB) "Cat B" 
            else "Mouse C"

        if (mouseC <= min) out(min)
        if (mouseC >= max) out(max)

        
        if (mouseC - min == max - mouseC) out(mouseC) 
        else if (mouseC - min > max - mouseC) out(max) 
        else out(min)

    }
        
    def main(args: Array[String]) {
        val cases = readInput()

        cases.foreach(c => println(who(c._1, c._2, c._3)))

    }
}
