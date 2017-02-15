"""
Detect a cycle in a linked list. Note that the head pointer may be 'None' if the list is empty.

A Node is defined as: 
 
    class Node(object):
        def __init__(self, data = None, next_node = None):
            self.data = data
            self.next = next_node
"""

def iterate(list, n):
    if list is None: 
        return None
    
    cur = list    
    for i in range(1, n):
        if cur.next is None:
            return None
        cur = cur.next
    return cur


def has_cycle(head):    
    iter1 = iterate(head, 1)
    iter2 = iterate(head, 2)
    while iter1 is not None and iter2 is not None:        
        if iter1 is iter2:
            return True
        iter1 = iterate(iter1, 1)
        iter2 = iterate(iter2, 2)        
    return False
    
    
