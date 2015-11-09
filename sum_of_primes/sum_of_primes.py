import functools


def sum_of_primes(limit):
    numbers_len = limit * 10
    numbers = [(x, 1) for x in range(2, numbers_len)]
    primes = []

    for n, i in numbers:
        if len(primes) == limit:
            break

        if i != 0:
            primes.append(n)
            for j in range(n, numbers_len, n):
                numbers[j-2] = (j, 0)

    return sum(primes)


if __name__ == '__main__':
    print(sum_of_primes(1000))
