"""
 Insert Node at the end of a linked list 
 head pointer input could be None as well for empty list
 Node is defined as
 
 class Node(object):
 
   def __init__(self, data=None, next_node=None):
       self.data = data
       self.next = next_node
 
 return back the head of the linked list in the below method
"""
def get_last(it):
    last = None
    while it is not None:        
        last = it
        it = it.next
    return last

def Insert(head, data):    
    last = get_last(head)
    if last is None:
        head = Node(data=data)
    else:
        last.next = Node(data=data)
    return head
