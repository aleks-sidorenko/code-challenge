import scala.collection._
    
object Solution {

    sealed trait Command
    final case class Enqueue(value: Int) extends Command
    final case object Dequeue extends Command
    final case object Print extends Command
    
    class Queue {
        private val input = new mutable.Stack[Int]()
        private val output = new mutable.Stack[Int]()
        
        def print() = {
            println(head)
        }
        
        
        def head = {
            if (output.isEmpty) prepare()
            output.head
        }
        
        def enqueue(value: Int) = {
            input.push(value)
        }
        
        def dequeue(): Int = {
            if (output.isEmpty) prepare()
            output.pop
        }
        
        private def prepare() = {
            if (!output.isEmpty) throw new IllegalStateException()
            while (!input.isEmpty) {
                output.push(input.pop)                
            }
        }
    }
    
    final val queue = new Queue()
    
    def act(cmd: Command) = {
        cmd match {
            case Print => queue.print
            case Enqueue(value) => queue.enqueue(value)
            case Dequeue => queue.dequeue
        }
    }
    
    def readInput(): List[Command] = {
        val sc = new java.util.Scanner (System.in)
        val q = sc.nextInt()
        
                                 
        val commands = new Array[Command](q)
        for (i <- 0 until q) {
            val id = sc.nextInt()
            commands(i) = id match {
                case 1 => Enqueue(sc.nextInt())
                case 2 => Dequeue
                case 3 => Print
            }            
        }
        commands.toList
    }
    
    
    def main(args: Array[String]) {
        val commands = readInput()
        commands.foreach { act _ }
        
    }
}
