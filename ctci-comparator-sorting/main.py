from functools import cmp_to_key

class Player:
    def __init__(self, name, score):
        self.name = name
        self.score = score
        
    def __repr__(self):
        "${name} ${score}".format(name=self.name, score=self.score)
        
    def comparator(a, b):
        ret = b.score - a.score
        if ret == 0:
            if b.name < a.name:
                ret = 1
            elif b.name > a.name:
                ret = -1
            else:
                ret = 0
        return ret
        
