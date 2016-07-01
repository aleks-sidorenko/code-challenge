import sys

# Solution is not optimal for memory
def array_rotation(array, d):
    m = d
    return array[m:] + array[:m]

line1 = next(sys.stdin)
n, d = map(int, line1.strip().split(" "))
array = list(next(sys.stdin).strip().split(" "))
print(' '.join(array_rotation(array=array, d=d)))
