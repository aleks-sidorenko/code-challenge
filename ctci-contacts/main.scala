object Solution {

    object Trie {
        def apply() : Trie = new TrieNode()
    }
    
    trait Trie {
        def add(word: String)
        def find(prefix: String): Seq[String]
    }
    
    private class TrieNode(char: Option[Char] = None, word: Option[String] = None)
        extends Trie {
        
        override def add(word: String) = {}
        override def find(prefix: String): Seq[String] = List.empty[String]
    }
    
    def readInput(): Seq[(String, String)] = {
        val sc = new java.util.Scanner (System.in)
        var n = sc.nextInt()
        var i = 0
        for (i <- 1 to n) yield sc.next() -> sc.next()
        
    }
    
    def main(args: Array[String]) {
        val trie = Trie()
            
        val input = readInput()
        input.foreach { 
            case (op, word) => op match {
                case "add" => trie.add(word)
                case "find" => println(trie.find(word).length)
            }
                      
        }
    }
}
