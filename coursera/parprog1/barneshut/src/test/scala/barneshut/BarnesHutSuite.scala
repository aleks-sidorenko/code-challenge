package barneshut

import java.util.concurrent._
import scala.collection._
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import common._
import scala.math._
import scala.collection.parallel._
import barneshut.conctrees.ConcBuffer

@RunWith(classOf[JUnitRunner])
class BarnesHutSuite extends FunSuite {

  // test cases for quad tree

import FloatOps._
  test("Empty: center of mass should be the center of the cell") {
    val quad = Empty(51f, 46.3f, 5f)
    assert(quad.massX == 51f, s"${quad.massX} should be 51f")
    assert(quad.massY == 46.3f, s"${quad.massY} should be 46.3f")
  }

  test("Empty: mass should be 0") {
    val quad = Empty(51f, 46.3f, 5f)
    assert(quad.mass == 0f, s"${quad.mass} should be 0f")
  }

  test("Empty: total should be 0") {
    val quad = Empty(51f, 46.3f, 5f)
    assert(quad.total == 0, s"${quad.total} should be 0")
  }

  test("Leaf with 1 body") {
    val b = new Body(123f, 18f, 26f, 0f, 0f)
    val quad = Leaf(17.5f, 27.5f, 5f, Seq(b))

    assert(quad.mass ~= 123f, s"${quad.mass} should be 123f")
    assert(quad.massX ~= 18f, s"${quad.massX} should be 18f")
    assert(quad.massY ~= 26f, s"${quad.massY} should be 26f")
    assert(quad.total == 1, s"${quad.total} should be 1")
  }


  test("Leaf.insert(b) should return a new Fork if size > minimumSize") {



    val b1 = new Body(10f, 18f, 26f, 0f, 0f)
    val b2 = new Body(10f, 18f, 26f, 0f, 0f)
    val quad = Leaf(20.0f, 30.0f, 10.0f, Seq(b1))
    val res = quad.insert(b2)
    assert(res.isInstanceOf[Fork], "should be a Fork")
    assert(res.total == 2, s"${quad.total} should be 2")

  }


  test("Leaf with 1 body when adding 1 body") {
    val b = new Body(50f, 10f, 20f, 0f, 0f)
    val quad = Leaf(17.5f, 27.5f, 5f, Seq(b))

    val b2 = new Body(50f, 20f, 30f, 1f, 1f)
    val quad2 = quad.insert(b2)

    assert(quad2.mass ~= 100f, s"${quad2.mass} should be 100f")
    assert(quad2.massX ~= 15f, s"${quad2.massX} should be 15f")
    assert(quad2.massY ~= 25f, s"${quad2.massY} should be 25f")
    assert(quad2.total == 2, s"${quad2.total} should be 2")
    assert(quad2.size ~= 5f, s"${quad2.size} should be 5f")
  }


  test("Fork with 3 empty quadrants and 1 leaf (nw)") {
    val b = new Body(123f, 18f, 26f, 0f, 0f)
    val nw = Leaf(17.5f, 27.5f, 5f, Seq(b))
    val ne = Empty(22.5f, 27.5f, 5f)
    val sw = Empty(17.5f, 32.5f, 5f)
    val se = Empty(22.5f, 32.5f, 5f)
    val quad = Fork(nw, ne, sw, se)

    assert(quad.centerX == 20f, s"${quad.centerX} should be 20f")
    assert(quad.centerY == 30f, s"${quad.centerY} should be 30f")
    assert(quad.mass ~= 123f, s"${quad.mass} should be 123f")
    assert(quad.massX ~= 18f, s"${quad.massX} should be 18f")
    assert(quad.massY ~= 26f, s"${quad.massY} should be 26f")
    assert(quad.total == 1, s"${quad.total} should be 1")
  }

  test("Fork with 4 empty quadrants") {

    val nw = Empty(17.5f, 27.5f, 5f)
    val ne = Empty(22.5f, 27.5f, 5f)
    val sw = Empty(17.5f, 32.5f, 5f)
    val se = Empty(22.5f, 32.5f, 5f)
    val quad = Fork(nw, ne, sw, se)

    assert(quad.centerX == 20f, s"${quad.centerX} should be 20f")
    assert(quad.centerY == 30f, s"${quad.centerY} should be 30f")
    assert(quad.mass ~= 0f, s"${quad.mass} should be 0f")
    assert(quad.massX ~= 20f, s"${quad.massX} should be 20f")
    assert(quad.massY ~= 30f, s"${quad.massY} should be 30f")
    assert(quad.total == 0, s"${quad.total} should be 0")
  }

  test("Empty.insert(b) should return a Leaf with only that body") {
    val quad = Empty(51f, 46.3f, 5f)
    val b = new Body(3f, 54f, 46f, 0f, 0f)
    val inserted = quad.insert(b)
    inserted match {
      case Leaf(centerX, centerY, size, bodies) =>
        assert(centerX == 51f, s"$centerX should be 51f")
        assert(centerY == 46.3f, s"$centerY should be 46.3f")
        assert(size == 5f, s"$size should be 5f")
        assert(bodies == Seq(b), s"$bodies should contain only the inserted body")
      case _ =>
        fail("Empty.insert() should have returned a Leaf, was $inserted")
    }
  }

  // test cases for Body

  test("Body.updated should do nothing for Empty quad trees") {
    val b1 = new Body(123f, 18f, 26f, 0f, 0f)
    val body = b1.updated(Empty(50f, 60f, 5f))

    assert(body.xspeed == 0f)
    assert(body.yspeed == 0f)
  }

  test("Body.updated should take bodies in a Leaf into account") {
    val b1 = new Body(123f, 18f, 26f, 0f, 0f)
    val b2 = new Body(524.5f, 24.5f, 25.5f, 0f, 0f)
    val b3 = new Body(245f, 22.4f, 41f, 0f, 0f)

    val quad = Leaf(15f, 30f, 20f, Seq(b2, b3))

    val body = b1.updated(quad)

    assert(body.xspeed ~= 12.587037f)
    assert(body.yspeed ~= 0.015557117f)
  }

  // test cases for sector matrix

  test("'SectorMatrix.+=' should add a body at (25,47) to the correct bucket of a sector matrix of size 96") {
    val body = new Body(5, 25, 47, 0.1f, 0.1f)
    val boundaries = new Boundaries()
    boundaries.minX = 1
    boundaries.minY = 1
    boundaries.maxX = 97
    boundaries.maxY = 97
    val sm = new SectorMatrix(boundaries, SECTOR_PRECISION)
    sm += body
    val res = sm(2, 3).size == 1 && sm(2, 3).find(_ == body).isDefined
    assert(res, s"Body not found in the right sector")
  }

  test("'SectorMatrix.+=' should add a bodies at (-1,-1) & (100, 100) to the correct bucket of a sector matrix of size 96") {
    val body1 = new Body(5, -1, -1, 0.1f, 0.1f)
    val body2 = new Body(2, 100, 100, 0.1f, 0.1f)
    val boundaries = new Boundaries()
    boundaries.minX = 1
    boundaries.minY = 1
    boundaries.maxX = 97
    boundaries.maxY = 97
    val sm = new SectorMatrix(boundaries, SECTOR_PRECISION)
    sm += body1
    sm += body2
    var res = sm(0, 0).size == 1 && sm(0, 0).find(_ == body1).isDefined
    assert(res, s"Body not found in the right sector")

    res = sm(7, 7).size == 1 && sm(7, 7).find(_ == body2).isDefined
    assert(res, s"Body not found in the right sector")
  }

  test("'SectorMatrix.combine' should combine 2 matrix") {
    val body1 = new Body(5, 15, 15, 0.1f, 0.1f)
    val body2 = new Body(2, 16, 16, 0.1f, 0.1f)
    val boundaries = new Boundaries()
    boundaries.minX = 1
    boundaries.minY = 1
    boundaries.maxX = 97
    boundaries.maxY = 97
    val sm1 = new SectorMatrix(boundaries, SECTOR_PRECISION)
    sm1 += body1
    sm1 += body2


    val body3 = new Body(2, 14, 14, 0.1f, 0.1f)
    val sm2 = new SectorMatrix(boundaries, SECTOR_PRECISION)
    sm2 += body3

    val sm = sm1.combine(sm2)

    var res = sm(0, 0).size == 0
    assert(res, s"Body is found in the wrong sector")

    val conc = sm(1, 1)
    res = conc.size == 3 && conc.find(_ == body1).isDefined &&
      conc.find(_ == body2).isDefined && conc.find(_ == body3).isDefined
    assert(res, s"Body not found in the right sector")
  }


  test("'Simulator.updateBoundaries' should update small boundaries to include body") {
    val body1 = new Body(5, 100, 200, 0.1f, 0.1f)

    val boundaries = new Boundaries()
    boundaries.minX = 1
    boundaries.minY = 1
    boundaries.maxX = 97
    boundaries.maxY = 97

    val res = BarnesHut.simulator.updateBoundaries(boundaries, body1)
    assert(res.minX == 1 && res.maxX == 100 && res.minY == 1 && res.maxY == 200)
  }

  test("'Simulator.updateBoundaries' should not update boundaries if body is inside") {
    val body1 = new Body(5, 10, 20, 0.1f, 0.1f)

    val boundaries = new Boundaries()
    boundaries.minX = 1
    boundaries.minY = 1
    boundaries.maxX = 97
    boundaries.maxY = 97

    val res = BarnesHut.simulator.updateBoundaries(boundaries, body1)
    assert(res.minX == 1 && res.maxX == 97 && res.minY == 1 && res.maxY == 97)
  }

  test("'Simulator.mergeBoundaries' should merge 2 boundaries") {

    val boundaries1 = new Boundaries()
    boundaries1.minX = 10
    boundaries1.minY = 11
    boundaries1.maxX = 20
    boundaries1.maxY = 21

    val boundaries2 = new Boundaries()
    boundaries2.minX = 30
    boundaries2.minY = 31
    boundaries2.maxX = 50
    boundaries2.maxY = 51

    val res = BarnesHut.simulator.mergeBoundaries(boundaries1, boundaries2)

    assert((res.minX ~= 10f) && (res.maxX ~= 50f) && (res.minY ~= 11f) && (res.maxY ~= 51f))
  }
}

object FloatOps {
  private val precisionThreshold = 1e-4

  /** Floating comparison: assert(float ~= 1.7f). */
  implicit class FloatOps(val self: Float) extends AnyVal {
    def ~=(that: Float): Boolean =
      abs(self - that) < precisionThreshold
  }

  /** Long floating comparison: assert(double ~= 1.7). */
  implicit class DoubleOps(val self: Double) extends AnyVal {
    def ~=(that: Double): Boolean =
      abs(self - that) < precisionThreshold
  }

  /** Floating sequences comparison: assert(floatSeq ~= Seq(0.5f, 1.7f). */
  implicit class FloatSequenceOps(val self: Seq[Float]) extends AnyVal {
    def ~=(that: Seq[Float]): Boolean =
      self.size == that.size &&
        self.zip(that).forall { case (a, b) =>
          abs(a - b) < precisionThreshold
        }
  }
}

