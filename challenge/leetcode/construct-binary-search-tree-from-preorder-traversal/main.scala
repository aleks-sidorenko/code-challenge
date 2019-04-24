/**
 * Definition for a binary tree node.
 * class TreeNode(var _value: Int) {
 *   var value: Int = _value
 *   var left: TreeNode = null
 *   var right: TreeNode = null
 * }
 */
object Solution {
    def bstFromPreorder(preorder: Array[Int]): TreeNode = {
        
        def tree(v: Int, l: TreeNode = null, r: TreeNode = null) = {
            val res = new TreeNode(v)
            res.left = l
            res.right = r
            res
        }
        
        
        def range(root: Int, from: Int, to: Int): Option[Int] = {
            (from until to).filter(x => preorder(x) < root).lastOption
        }
        
        def loop(from: Int, to: Int): TreeNode = {
            if (from >= to) null 
            else {
                val root = preorder(from)
                val idx = range(root, from + 1, to).getOrElse(from)
                tree(root, loop(from + 1, idx + 1), loop(idx + 1, to))
                
            }
        }
        
        loop(0, preorder.length)
        
    }
}