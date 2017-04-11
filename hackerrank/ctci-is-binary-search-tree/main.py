""" Node is defined as
class node:
    def __init__(self, data):
        self.data = data
        self.left = None
        self.right = None
"""
MIN_INT = -1
MAX_INT = 10000000

def is_binary_search_tree(node, start, end):
    if node is None:
        return True

    if node.data < start or node.data > end:
        return False

    return is_binary_search_tree(node.left, start, node.data - 1) \
    and is_binary_search_tree(node.right, node.data + 1, end)

def check_binary_search_tree_(root):
    return is_binary_search_tree(root, MIN_INT, MAX_INT)
