import sys


def count_sum(a, k):
    count = 0
    for i in range(len(a) - 1):
        for j in range(i + 1, len(a)):
            if (a[i] + a[j]) % k == 0:
                count += 1
    return count

n, k = input().strip().split(' ')
n, k = [int(n), int(k)]
a = [int(i) for i in input().strip().split(' ')]

print(count_sum(a, k))
