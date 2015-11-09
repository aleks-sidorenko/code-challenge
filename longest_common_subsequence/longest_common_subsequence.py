from functools import reduce
import sys


def intersect(a, b):
    return list(set(a) & set(b))

def split_to_subsequences(sequence, length):
    assert len(sequence) >= length, "Sequence '{sequence}' must be longer than {length}"\
        .format(sequence=sequence, length=length)

    ret = []
    for i in range(0, len(sequence) - length):
        ret.append(sequence[i:i+length])
    return ret


def restrict_subsequences(subsequences, old_subsequences):
    ret = []
    for i in subsequences:
        if i[:-1] in old_subsequences:
            ret.append(i)
    return ret


def get_common_subsequences(sequences, old_subsequences, length):
    subsequences = reduce(intersect, map(lambda s: split_to_subsequences(s, length), sequences))
    if old_subsequences is not None:
        subsequences = restrict_subsequences(subsequences, old_subsequences)

    if len(subsequences) == 0:
        return old_subsequences

    return get_common_subsequences(sequences, subsequences, length + 1)


def get_longest_common_subsequence(sequences):
    sorted_sequences = sorted(sequences, key=len)
    return get_common_subsequences(sorted_sequences, None, 1)




with open(sys.argv[1], 'r') as test_cases:
    for test in test_cases:
        sequences = test.strip().split(";")
        for r in get_longest_common_subsequence(sequences):
            print(r)
