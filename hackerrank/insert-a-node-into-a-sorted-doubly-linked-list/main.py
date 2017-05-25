"""
 Insert a node into a sorted doubly linked list
 head could be None as well for empty list
 Node is defined as
 
 class Node(object):
 
   def __init__(self, data=None, next_node=None, prev_node = None):
       self.data = data
       self.next = next_node
       self.prev = prev_node

 return the head node of the updated list 
"""
def print_list(head):
    print("head->",end='')
    node = head.next
    while node is not None:
        print("{data}->".format(data=node.data), end='')
        node = node.next
    print("null")

def find_node(head, data):
    node = head.next
    prev = head
    while node is not None and node.data < data:
        prev = node
        node = node.next    
    node = prev
    return node

def sorted_list_insert(head, data):        
    if head is None:
        head = Node()
               
    node = find_node(head, data)
        
    new_node = Node(data=data)
    
    old_next = node.next
    node.next = new_node
    new_node.prev = node
    if old_next is not None:
        new_node.next = old_next
        old_next.prev = new_node        
        
    return head
  
  
def SortedInsert(head, data):
    return sorted_list_insert(head, data)
  
  
  
  
  
  
