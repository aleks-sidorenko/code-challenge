from functools import reduce
import sys


def product_of_other_numbers(array):
    total = reduce(lambda x, y: x*y, array)
    return list(map(lambda x: int(total/x), array))


with open(sys.argv[1], 'r') as test_cases:
    for test in test_cases:
        array = list(map(int, test.strip().split(";")))
        result = product_of_other_numbers(array)
        print(result)
