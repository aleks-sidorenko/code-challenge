# Definition for singly-linked list.
"""
class ListNode(object):
     def __init__(self, x):
         self.val = x
         self.next = None
"""

class Solution(object):
    def addTwoNumbers(self, l1, l2):
        
        """
        :type l1: ListNode
        :type l2: ListNode
        :rtype: ListNode
        """
        
        def add(l1, l2, extra):
            
            if l1 is None and l2 is None and extra == 0:
                return None
                
            l1val = l1.val if l1 is not None else 0
            l2val = l2.val if l2 is not None else 0
            total = l1val + l2val + extra
            newVal = total % 10
            newExtra = 1 if total >= 10 else 0
            
            ret = ListNode(newVal)
            ret.next = add(l1.next if l1 is not None else None, l2.next if l2 is not None else None, newExtra)
            return ret
            
        return add(l1, l2, 0)
        