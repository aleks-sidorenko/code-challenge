import itertools
import math
import sys


def split_amount(amount, denominations):
    def check(coins):
        return amount == sum(map(lambda x, y: x * y, denominations, coins))

    max_coins = int(math.ceil(amount / denominations[0]))
    variants = itertools.product(range(max_coins + 1), repeat=len(denominations))

    return (x for x in variants if check(x))


with open(sys.argv[1], 'r') as input_file:
    print(list(split_amount(amount=int(next(input_file)),
                            denominations=list(map(int, next(input_file).strip().split(";"))))))
