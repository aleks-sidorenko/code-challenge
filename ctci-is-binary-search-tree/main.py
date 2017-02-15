""" Node is defined as
class node:
    def __init__(self, data):
        self.data = data
        self.left = None
        self.right = None
"""
max_int = 10000000

def tree_min(node):
    if node is None:
        return max_int
    
    left = tree_min(node.left)
    right = tree_min(node.right)
    
    return min([left, node.data, right])


def tree_max(node):
    if node is None:
        return 0
    
    left = tree_max(node.left)
    right = tree_max(node.right)
    
    return max([left, node.data, right])


def is_binary_search_tree(node):
    if node is None:
        return True
    
    if tree_max(node.left) >= node.data:
        return False

    if tree_min(node.right) <= node.data:
        return False
    
    return is_binary_search_tree(node.left) and is_binary_search_tree(node.right)

    
def check_binary_search_tree_(root):
    return is_binary_search_tree(root)

