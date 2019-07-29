/**
 * Definition for a binary tree node.
 * class TreeNode(var _value: Int) {
 *   var value: Int = _value
 *   var left: TreeNode = null
 *   var right: TreeNode = null
 * }
 */
object Solution {
    def bstToGst(root: TreeNode): TreeNode = {
 
        def loop(node: TreeNode, add: Int = 0): (TreeNode, Int) = {
            if (node == null) (null, 0)
            else {
                val (right, rs) = loop(node.right, add)
                val v = node.value + rs + add
                val (left, ls) = loop(node.left, v)
                val ret = new TreeNode(v)
                ret.right = right
                ret.left = left
                ret -> (node.value + ls + rs)
                
            }
        }
        val (ret, _) = loop(root)
        ret
    }
}