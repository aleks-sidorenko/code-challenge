import scala.collection._
import scala.collection.JavaConverters._
    
object Solution {

    final val CHARS_COUNT = 26
        
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
            
        private val children: Array[TrieNode] = new Array[TrieNode](CHARS_COUNT)
            
        final override def add(word: String) = {
            addAt(word, 0)
        }
        
        private def getIndex(char: Char) = char - 'a'
            
        private def getChild(char: Char): TrieNode = {            
            children(getIndex(char))
        }
        
        private def getChildOrCreate(char: Char): TrieNode = {            
            var child = children(getIndex(char))
            if (child eq null) {
                child = new TrieNode
                children(getIndex(char)) = child
            }
            child
        }
        
        
        private def addAt(word: String, level: Int): Unit = {
            
            count += 1;
            
            val index = level
            if (index == word.length) {
                return
            }
                                                            
            val char = word(index)
                        
            val child = getChildOrCreate(char)
            child.addAt(word, level + 1)                        
        }
                
        final override def find(prefix: String): Int = {
            findAt(prefix, 0)
        }
        
        private final def findAt(prefix: String, level: Int) : Int = {
            if (level < prefix.length) {
                val char = prefix(level)
                val child = getChild(char)
                if (child ne null) {
                    child.findAt(prefix, level + 1)
                } else {
                    0
                }
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
