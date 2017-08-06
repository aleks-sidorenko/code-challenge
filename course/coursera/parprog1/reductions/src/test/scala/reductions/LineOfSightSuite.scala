package reductions

import java.util.concurrent._
import scala.collection._
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import common._
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory

@RunWith(classOf[JUnitRunner]) 
class LineOfSightSuite extends FunSuite {
  import LineOfSight._

  test("lineOfSight should correctly handle an array of size 4") {
    val output = new Array[Float](4)
    lineOfSight(Array[Float](0f, 1f, 8f, 9f), output)
    assert(output.toList == List(0f, 1f, 4f, 4f))
  }


  test("upsweepSequential should correctly handle the chunk 1 until 4 of an array of 4 elements") {
    val res = upsweepSequential(Array[Float](0f, 1f, 8f, 9f), 1, 4)
    assert(res == 4f)
  }


  test("downsweepSequential should correctly handle a 4 element array when the starting angle is zero") {
    val output = new Array[Float](4)
    downsweepSequential(Array[Float](0f, 1f, 8f, 9f), output, 0f, 1, 4)
    assert(output.toList == List(0f, 1f, 4f, 4f))
  }


  test("downsweepSequential should correctly handle a 10 element array when the starting angle is zero") {
    val output = new Array[Float](10)
    downsweepSequential(Array[Float](0f, 1f, 8f, 9f, 7f, 6f, 5f, 2f, 64f, 18f), output, 0f, 0, 10)
    assert(output.toList == List(0.0f, 1.0f, 4.0f, 4.0f, 4.0f, 4.0f, 4.0f, 4.0f, 8.0f, 8.0f))
  }


  test("parLineOfSight should correctly handle an array of size 4") {
    val output = new Array[Float](4)
    parLineOfSight(Array[Float](0f, 1f, 8f, 9f), output, 2)
    assert(output.toList == List(0f, 1f, 4f, 4f))
  }

  test("parLineOfSight should correctly handle an array of size 10") {
    val output = new Array[Float](10)
    parLineOfSight(Array[Float](0f, 1f, 8f, 9f, 7f, 6f, 5f, 2f, 64f, 18f), output, 3)
    assert(output.toList == List(0.0f, 1.0f, 4.0f, 4.0f, 4.0f, 4.0f, 4.0f, 4.0f, 8.0f, 8.0f))
  }

}

