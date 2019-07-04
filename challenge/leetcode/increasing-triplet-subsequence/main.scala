/**
 * Definition for singly-linked list.
 * class ListNode(var _x: Int = 0) {
 *   var next: ListNode = null
 *   var x: Int = _x
 * }
 */
object Solution {
    def swapPairs(head: ListNode): ListNode = {
        if (head == null || head.next == null) head
        else {
            val newHead = head.next
            val tail = newHead.next
            newHead.next = head
            head.next = swapPairs(tail)
            newHead
        }
    }
}