import scala.collection._
    
object Solution {

    object Trie {
        def apply() : Trie = new TrieNode()
    }
    
    trait Trie {
        def add(word: String)
        def find(prefix: String): Seq[String]
    }
    
    private class TrieNode(private var data: Option[String] = None)
        extends Trie {
     
        private val children: mutable.Map[Char, TrieNode] = new mutable.HashMap[Char, TrieNode]()
            
        override def add(word: String) = {
            addAt(word, 0)
        }
        
        private def addAt(word: String, index: Int): Unit = {
            
            val char = word(index)
                
            index match {                
                case i if i == word.length - 1 => data = Some(word)
                case i => val child = children.getOrElseUpdate(char, new TrieNode()); child.addAt(word, i + 1)                                
            }
        }
        
        override def find(prefix: String): Seq[String] = {
            
        }
        
        private def findAt(prefix: String, index: Int) : Seq[String] = {
            index match {                
                case i if i >= prefix.length => 
                    
                case i => val child = children.getOrElseUpdate(char, new TrieNode()); child.addAt(word, i + 1)                                
            }
            val char = prefix(index)
            
            val child = children.get(char)
                
            word match {
                Some(w) => w :: child.flatMap(_.findAt(prefix, index + 1)).getOrElse(Nil)
            }
        }
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
