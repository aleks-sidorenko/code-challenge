"""
Detect a cycle in a linked list. Note that the head pointer may be 'None' if the list is empty.

A Node is defined as: 
 
    class Node(object):
        def __init__(self, data = None, next_node = None):
            self.data = data
            self.next = next_node
"""

def iter(it, num = 1):
    for i in range(0, num):
        if it.next is not None:
            it = it.next
        else:
            return None
    return it
    

def has_cycle(head):
    fast = head.next
    slow = head
    
    while fast is not None:
        if fast is slow:
            return True
                
        fast = iter(fast, 2)
        slow = iter(slow.next)
    

    return False