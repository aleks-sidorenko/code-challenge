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
    
    private class TrieNode(private var data: Option[String] = None)
        extends Trie {
     
        private var count: Int = 0
            
        private val children: mutable.Map[Char, TrieNode] = new java.util.HashMap[Char, TrieNode]().asScala
            
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
            
             
            val d = if (index == word.length - 1) Some(word) else None    
            val child = children.getOrElseUpdate(char, new TrieNode(data=d))
            child.addAt(word, level + 1)                        
        }
        
        def printTree(): Unit = {
            println(s"count=${count}, data=${data}")
            children.foreach( { case (ch, trie) => trie.printTree } )
        }
        
        final override def find(prefix: String): Int = {
            findAt(prefix, 0)
        }
        
        @inline
        private def findAt(prefix: String, level: Int) : Int = {
            val char: Option[Char] = if (level < prefix.length) Some(prefix(level)) else None
            
            val next: Int = char match {                
                case Some(ch) => children.get(ch).map { trie => trie.findAt(prefix, level + 1) } getOrElse 0
                    
                case None => count
            } 
            next
                         
        }
        
    }
    
    
    def readInput(): Seq[(String, String)] = {
        val sc = new java.util.Scanner (System.in)
        var n = sc.nextInt()
        var i = 0
        for (i <- 1 to n) yield sc.next() -> sc.next()
        
    }
    
    def main(args: Array[String]) {
        val trie = new TrieNode()
            
        val input = readInput()
        input.foreach { 
            case (op, word) => op match {
                case "add" => trie.add(word)
                case "find" => println(trie.find(word))
            }
                      
        }        
       
    }
}
