package captify.test.java;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static captify.test.java.TestAssignment.*;

public class SparseIteratorsApp {
  private static void runTests(int sampleAfterNum, int sparsityMin, int sparsityMax, int approximateExtent) {

    final List<Iterator<BigInteger>> iterators =
      Stream
        .iterate(2, (a) -> a + 2)
        .limit(512)
        .map(SparseIterators::iteratorForBitCount)
        .collect(Collectors.toList());

    final long mergeStartedAt = System.currentTimeMillis();

    final Iterator<BigInteger> mergedIterator =
      mergeIterators(iterators);

    final Iterator<BigInteger> numbers =
      sampleAfter(mergedIterator, sampleAfterNum, 10);
    final List<BigInteger> numbersList = new ArrayList<>();
    numbers.forEachRemaining(numbersList::add);

    final long mergeFinishedAt = System.currentTimeMillis();
    final long mergeMillis = mergeFinishedAt - mergeStartedAt;

    System.out.println("sampled merged iterator after " + sampleAfterNum + " in " + mergeMillis + " millis:");
    numbersList.iterator()
      .forEachRemaining(num -> System.out.println( num + " " + num.bitCount()));

    final long approximatesStartedAt = System.currentTimeMillis();
    final Map<Integer, Future<Double>> approximatesRes =
      approximatesFor(sparsityMin, sparsityMax, approximateExtent);
    final long approximatesFinishedAt = System.currentTimeMillis();
    final long approximatesMillis = approximatesFinishedAt - approximatesStartedAt;
    final int cores = Runtime.getRuntime().availableProcessors();

    System.out.println("approximate sparsities in " + approximatesMillis + " millis by " + approximateExtent + " elems with " + cores + " cores:");
    approximatesRes.entrySet().forEach(e -> {
      final Future<Double> future = e.getValue();
      String futureAsString;
      if (!future.isDone()) {
        futureAsString = "<incomplete>";
      } else {
        try {
          futureAsString = String.valueOf(future.get());
        } catch (InterruptedException e1) {
          futureAsString = "<interrupted on get()>";
        } catch (ExecutionException e1) {
          futureAsString = "failed: " + e1.getCause().getMessage();
        }
      }
      System.out.println(e.getKey() + " -> " + futureAsString);
    });
  }


  public static void main(String[] args) {
    //  simple less loaded tests
    runTests(1570786, 2, 8, 100000);

    //  more intensive tests with just a bit of exceptions
    runTests(135914081, 0, 24, 10000000);
  }
}
