// Start writing your ScalaFiddle code here
object Solution {
    def partitionLabels(s: String): List[Int] = {
        
        def add(parts: Seq[Set[Char]], ch: Char): Seq[Set[Char]] = {            
            (0 until parts.length).foldLeft[Option[Int]](None) { 
                case (None, i) if parts(i)(ch) => Some(i)
                case (acc, _) => acc
                
            }.map { ind =>                
                val n = ind + 1
                val h = parts.take(n).reduce(_ | _)
                val t = parts.drop(n)
                h +: t
            }.getOrElse(Set(ch) +: parts)
                        
        }
        
        def loop(chars: List[Char], parts: Seq[Set[Char]]): Seq[Set[Char]] = {
            chars match {
                case x :: xs => loop(xs, add(parts, x))                
                case _ => parts
            }
        }
        
        val sets = loop(s.toList, Seq.empty[Set[Char]]).reverse        
        val counts = s.toList.groupBy(identity).mapValues(_.size)        
        sets.map { s =>s.toList.map(x => counts(x)).sum }.toList
    }
}