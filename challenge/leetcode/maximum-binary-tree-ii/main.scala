/**
 * Definition for a binary tree node.
 * class TreeNode(var _value: Int) {
 *   var value: Int = _value
 *   var left: TreeNode = null
 *   var right: TreeNode = null
 * }
 */
object Solution {
    
    
    def tree(v: Int, l: TreeNode = null, r: TreeNode = null) = {
        val ret = new TreeNode(v)
        ret.left = l
        ret.right = r
        ret
    }

    
    def insertIntoMaxTree(root: TreeNode, value: Int): TreeNode = {
        def loop(n: TreeNode): TreeNode = {
            if (n == null) tree(value)
            else if (value > n.value) {
                tree(value, n, null)
            } else {
                n.right = loop(n.right)
                n
            }            
        }
        loop(root)
        
    }
}