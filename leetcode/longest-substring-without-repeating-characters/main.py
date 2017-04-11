class Solution(object):
    def lengthOfLongestSubstring(self, s):
        """
        :type s: str
        :rtype: int
        """
        def get_char(s, i):
            return s[i:i+1]
        
        def dist(i, j):
            return abs(i - j) + 1
        
        unique = dict()
        j = 0
        max_len = 0
        for i in xrange(len(s)):
            char = get_char(s, i)
            if char in unique:
                j = max(j, unique[char] + 1)
                
            unique[char] = i
            max_len = max(max_len, dist(i, j))
        
        return max_len
