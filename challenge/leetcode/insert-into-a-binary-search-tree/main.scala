/**
 * Definition for a binary tree node.
 * class TreeNode(var _value: Int) {
 *   var value: Int = _value
 *   var left: TreeNode = null
 *   var right: TreeNode = null
 * }
 */
object Solution {
    def insertIntoBST(root: TreeNode, value: Int): TreeNode = {
        def loop(n: TreeNode): TreeNode = {
            if (n == null) new TreeNode(value)
            else if (n.value > value) {
                n.left = loop(n.left)
                n
            }
            else {
                n.right = loop(n.right)
                n
            }
        }
        
        loop(root)
    }
}