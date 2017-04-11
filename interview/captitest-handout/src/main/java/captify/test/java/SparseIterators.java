package captify.test.java;

import java.math.BigInteger;
import java.util.Iterator;

/**
 * Boring utilities to generate your test data.
 */
public class SparseIterators {

  static BigInteger bitPositionsToBigInteger(int[] bitPositions) {
    if (bitPositions.length == 0) {
      return BigInteger.ZERO;
    }

    final byte[] bigIntBytes = new byte[1 + (bitPositions[bitPositions.length - 1] + 7) / 8];
    for (final int setPos : bitPositions) {
      final int setByte = setPos / 8;
      final int setOffset = setPos % 8;
      bigIntBytes[bigIntBytes.length - 1 - setByte] |= ((byte) 1) << setOffset;
    }

    return new BigInteger(bigIntBytes);
  }

  public static Iterator<BigInteger> iteratorForBitCount(final int bitCount) {
    if (bitCount < 0) {
      throw new IllegalArgumentException("negative bit count: " + bitCount);
    }

    if (bitCount == 0) {
      return java.util.Collections.singletonList(BigInteger.ZERO).iterator();
    }

    return new Iterator<BigInteger>() {
      final int[] bitPositions = new int[bitCount];
      {
        for (int idx = 0; idx < bitPositions.length; idx++) {
          bitPositions[idx] = idx;
        }
      }

      @Override public boolean hasNext() {
        return true;
      }

      @Override public BigInteger next() {
        final BigInteger result = bitPositionsToBigInteger(bitPositions);

        int advancePosition = 0;
        while (
          advancePosition < bitPositions.length - 1 &&
            bitPositions[advancePosition] + 1 == bitPositions[advancePosition + 1]
          ) {
          advancePosition += 1;
        }

        bitPositions[advancePosition] += 1;

        for (int idx = 0; idx < advancePosition; idx++) {
          bitPositions[idx] = idx;
        }

        return result;
      }
    };
  }
}
