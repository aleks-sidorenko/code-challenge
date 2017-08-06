package stackoverflow

import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import java.io.File

@RunWith(classOf[JUnitRunner])
class StackOverflowSuite extends FunSuite with BeforeAndAfterAll {

  lazy val testObject = new StackOverflow {

    @transient lazy val conf: SparkConf = new SparkConf().setMaster("local").setAppName("StackOverflow")
    @transient lazy val sc: SparkContext = new SparkContext(conf)


    override val langs =
      List(
        "JavaScript", "Java", "PHP", "Python", "C#", "C++", "Ruby", "CSS",
        "Objective-C", "Perl", "Scala", "Haskell", "MATLAB", "Clojure", "Groovy")
    override def langSpread = 50000
    override def kmeansKernels = 45
    override def kmeansEta: Double = 20.0D
    override def kmeansMaxIterations = 120
  }

  test("testObject can be instantiated") {
    val instantiatable = try {
      testObject
      true
    } catch {
      case _: Throwable => false
    }
    assert(instantiatable, "Can't instantiate a StackOverflow object")
  }

  test("'groupedPostings' should group answers & questions") {

    import testObject._

    val postings = sc.parallelize(List(
      Posting(1, 1, None, None, 1, Some("Scala")),
      Posting(2, 2, None, Some(1), 1, None),
      Posting(2, 3, None, Some(1), 1, None),
      Posting(1, 4, None, None, 1, Some("Java")))
    )

    val grouped = groupedPostings(postings).collect().toMap
    assert(grouped(1).size == 2, "Scala has 2 answers")
    assert(!grouped.contains(4), "Java has no answers")
  }


  test("'scoredPostings' should return proper scoring") {


    import testObject._

    val postings = sc.parallelize(List(
      Posting(1, 1, None, None, 1, Some("Scala")),
      Posting(2, 2, None, Some(1), 9, None),
      Posting(2, 3, None, Some(1), 11, None),
      Posting(1, 4, None, None, 2, Some("Java")),
      Posting(2, 5, None, Some(4), 7, None)
    )
    )

    val grouped = groupedPostings(postings)

    val scored = scoredPostings(grouped).collect().toList
    assert(scored.contains(Posting(1, 1, None, None, 1, Some("Scala")) -> 11))
    assert(scored.contains(Posting(1, 4, None, None, 2, Some("Java")) -> 7))
  }

  test("'scoredPostings' in sampled stackoverflow dataset should contain following tuples") {
    import testObject._

    val lines = sc.textFile(this.getClass.getResource("/stackoverflow/stackoverflow.csv").getPath)
    val raw = rawPostings(lines)
    val grouped = groupedPostings(raw)
    val scored = scoredPostings(grouped).collect()

    assert(scored.contains(Posting(1,6,None,None,140,Some("CSS")) -> 67))
    assert(scored.contains(Posting(1,42,None,None,155,Some("PHP")) -> 89))
    assert(scored.contains(Posting(1,72,None,None,16,Some("Ruby")) -> 3))
    assert(scored.contains(Posting(1,126,None,None,33,Some("Java")) -> 30))
    assert(scored.contains(Posting(1,174,None,None,38,Some("C#")) -> 20))
  }

  test("'vectorPostings' in sampled stackoverflow dataset should contain following tuples") {
    import testObject._

    val lines = sc.textFile(this.getClass.getResource("/stackoverflow/stackoverflow.csv").getPath)
    val raw = rawPostings(lines)
    val grouped = groupedPostings(raw)
    val scored = scoredPostings(grouped)
    val vectors = vectorPostings(scored).collect()
    assert(vectors.size == 2121822, "Incorrect number of vectors: " + vectors.size)
    assert(vectors.contains((350000,67)))
    assert(vectors.contains((100000,89)))
    assert(vectors.contains((300000,3)))
    assert(vectors.contains((50000,30)))
    assert(vectors.contains((200000,20)))
  }



  test("'clusterResults' running just 1 iteration on the sampled stackoverflow dataset should return correct result") {

    import testObject._

    val lines = sc.textFile(this.getClass.getResource("/stackoverflow/stackoverflow.csv").getPath)
    val raw = rawPostings(lines)
    val grouped = groupedPostings(raw)
    val scored = scoredPostings(grouped)
    val vectors = vectorPostings(scored)


    val means = kmeans(sampleVectors(vectors), vectors, iter = kmeansMaxIterations, debug = false)
    val results = clusterResults(means, vectors)
    assert(results.contains(("Python",100.0d,1740,8)))
  }

  test("'clusterResults' running on the sampled stackoverflow dataset should return correct result") {

    import testObject._

    val lines = sc.textFile(this.getClass.getResource("/stackoverflow/stackoverflow.csv").getPath)
    val raw = rawPostings(lines)
    val grouped = groupedPostings(raw)
    val scored = scoredPostings(grouped)
    val vectors = vectorPostings(scored)
    assert(vectors.count() == 2121822, "Incorrect number of vectors: " + vectors.count())

    val means = kmeans(sampleVectors(vectors), vectors, debug = false)
    val results = clusterResults(means, vectors)
    assert(results.contains(("PHP",100.0d,160,34)))
  }
}
