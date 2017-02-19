import scala.collection._
import scala.collection.JavaConverters._
    
object Solution {

    object Trie {
        def apply() : Trie = new TrieNode()
    }
    
    trait Trie {
        def add(word: String)
        def find(prefix: String): Int
    }
    
    private class TrieNode()
        extends Trie {
     
        private var count: Int = 0
            
        private val children: mutable.Map[Char, TrieNode] = new mutable.HashMap[Char, TrieNode]()
            
        final override def add(word: String) = {
            addAt(word, 0)
        }
        
        
        private def addAt(word: String, level: Int): Unit = {
            
            count += 1;
            
            val index = level
            if (index == word.length) {
                return
            }
            
                                                
            val char = word(index)
                                     
            val child = children.getOrElseUpdate(char, new TrieNode())
            child.addAt(word, level + 1)                        
        }
                
        final override def find(prefix: String): Int = {
            findAt(prefix, 0)
        }
        
        private final def findAt(prefix: String, level: Int) : Int = {
            if (level < prefix.length) {
                val char = prefix(level)
                children.get(char).map { trie => trie.findAt(prefix, level + 1) } getOrElse 0
            } else {
                count
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
                case "find" => println(trie.find(word))
            }
                      
        }        
       
    }
}
