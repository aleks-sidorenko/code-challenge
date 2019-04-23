/**
 * Definition for a binary tree node.
 * class TreeNode(var _value: Int) {
 *   var value: Int = _value
 *   var left: TreeNode = null
 *   var right: TreeNode = null
 * }
 */
object Solution {
    def constructMaximumBinaryTree(nums: Array[Int]): TreeNode = {
        
        def tree(v: Int, l: TreeNode, r: TreeNode) = {
            val ret = new TreeNode(v)
            ret.left = l
            ret.right = r
            ret
        }
        
        def loop(from: Int, to: Int): TreeNode = {
            if (from > to) null
            else {
                val max = (from to to).maxBy(nums(_))
                val ((from1, to1), (from2, to2)) = (from -> (max - 1)) -> ((max + 1) -> to)
                tree(nums(max), loop(from1, to1), loop(from2, to2))
            }
            
        }  
        
        loop(0, nums.length - 1)
    }
}