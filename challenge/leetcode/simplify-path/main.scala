object Solution {
    sealed trait Part  
    object Part {
        final val delim = "/"
        
        case object Up extends Part
        case object This extends Part
        case object Root extends Part
        case class Folder(name: String) extends Part
        
        
        def parse(str: String): Part = {
            str match {
                case ".." => Up
                case "." => This
                case "" => This
                case _ => Folder(str)
            }
        }
        
        def repr(part: Part): String = {
            part match {
                case Folder(f) => f
                case Root => ""
            }
        }
        
        
        def repr(ps: List[Part]): String = {
            ps match {
                case Root :: xs => delim + xs.map(Part.repr).mkString(delim)
            }
        }
        
        def reduce(ps: List[Part]): List[Part] = {            
            Root :: (ps.foldLeft(List.empty[Part]) { case (acc, p) =>
                p match { 
                    case This => acc
                    case Up => acc.drop(1)
                    case f @ Folder(_) => f :: acc
                }
            }).reverse
        }
    }
    
    
    def simplifyPath(path: String): String = {        
        import Part._
        repr(reduce(path.split(delim).toList.map(parse)))
    }
}