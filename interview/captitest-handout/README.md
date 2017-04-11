This simple project requires sbt, JDK 8 and scala 2.10.

There are two alternative and mostly identical tasks, one of which is in Java and other in Scala.

In both versions there is one file to be edited, either
    src/main/java/captify/test/java/TestAssignment.java 
or 
    src/main/scala/captify/test/scala/TestAssignment.scala

The methods to fill are documented and throw placeholder exceptions. 

Also, looking at the test harness could clarify the assignments a bit more, so see
    src/main/java/captify/test/java/SparseIteratorsApp.java
or 
    src/main/scala/captify/test/scala/TestAssignment.scala

To run this you just issue `sbt run` in the project root and then choose accordingly to run respective harness.
This of course presumes you've installed all the requirements mentioned above.

Requirements for this are:
 * publishing your solution on any public resource (github included) IS STRICTLY NOT ALLOWED and COULD VOID YOUR SUBMISSION
 * local git repo with initial commit containing original code and your edits as separate commit(s) IS REQUIRED
 * including one representative output of your local run(s) IS REQUIRED
 * the first, smaller, part of harness ( call to runTests(1570786, 2, 8, 100000) ) MUST complete in under 30 minutes
   * the time has been measured on 4-logical-core machine (Intel Core i7-3537U CPU @ 2.00GHz)

Additional plus-es are:
 * try to show reasonably functional style - avoid side-effects and mutable state, as long as it does not impair performance
 * adding some unit-tests or several other test harnesses is recommended, but not required
 * for efficient implementations, full harness (both parts) should complete in under 2 minutes
 * there is no need to extend Java VM memory limits for harness to complete successfully

Time required to complete the task should generally be under 6 hours, with some simple test cases added.
In case you spend more time (to add some recommended or otherwise interesting stuff),
  please commit a bit more often - so that your actual track record is visible.
