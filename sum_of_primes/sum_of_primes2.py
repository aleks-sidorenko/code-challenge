import functools

def gen_next(start):
    n = start - 1
    while True:
        n += 1
        yield n


def is_prime(n, primes):
    for p in primes:
        if n % p == 0:
            return False
    return True

def sum_of_primes(limit):
    primes = []

    for i in gen_next(2):
        if len(primes) == limit:
            break
        if is_prime(i, primes):
            primes.append(i)

    return sum(primes)


if __name__ == '__main__':
    print(sum_of_primes(1000))
