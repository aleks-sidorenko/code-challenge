object Solution {

    def count(str: String) = str.filter(c => c >= 'A' && c <= 'Z').length + 1
        
    def main(args: Array[String]) {
        val sc = new java.util.Scanner (System.in);
        var s = sc.next();
        
        println(count(s))
    }
}
