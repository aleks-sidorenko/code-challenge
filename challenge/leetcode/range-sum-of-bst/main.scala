object Solution {
    def rangeSumBST(root: TreeNode, L: Int, R: Int): Int = {
        
        def loop(node: TreeNode): Int = {
            if (node == null) 0
            else {
                node.value match {
                    case x if x >= L && x <= R =>
                        x + loop(node.left) + loop(node.right)
                    case x if x < L =>
                        loop(node.right)
                    case x if x > R =>
                        loop(node.left)
                    case _ => 0
                }
            }
        }
        loop(root)
    }
}