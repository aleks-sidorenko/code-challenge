object Solution {
    def partitionLabels(s: String): List[Int] = {
        
        def add(parts: List[Set[Char]], ch: Char): List[Set[Char]] = ???
        
        def loop(chars: List[Char], parts: List[Set[Char]]): List[Set[Char]] = {
            chars match {
                case x :: xs => loop(xs, add(parts, x))                
                case _ => parts
            }
        }
        
        loop(s.toList, List.empty[Set[Char]]).reverse.map(_.size)
        
    }
}