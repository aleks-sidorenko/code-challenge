package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

trait ManipulationTest extends FunSuite with Checkers {
  self: ExtractionTest =>

  test("'makeGrid' should return correct grid") {
    val cases = List(
      (90, -179) -> -1.70d,
      (0, 0) -> 1.66d

    )

    val grid = Manipulation.makeGrid(locateAverage)

    cases.foreach { case ((x, y), t) => assert( grid(x, y) === t) }
  }


  test("'average' should return correct grid") {
    val cases = List(
      (50, 50) -> 2.007d,
      (90, -179) -> 2.349d,
      (0, 0) -> 2.5d
    )

    val list1 = Locations.kiev -> 1.0d :: Locations.newYork -> 2.0d :: Locations.zero -> 0.0d :: Nil
    val list2 = Locations.kiev -> 3.0d :: Locations.newYork -> 4.0d :: Locations.zero -> 5.0d :: Nil

    val grid = Manipulation.average(Seq(list1, list2))

    cases.foreach { case ((x, y), t) => assert( grid(x, y) === t) }
  }


  test("'deviation' should return correct grid") {
    val cases = List(
      (50, 50) -> -2.02d,
      (90, -179) -> -2.15d,
      (0, 0) -> -5d
    )

    val list = Locations.kiev -> 1.0d :: Locations.newYork -> 2.0d :: Locations.zero -> 0.0d :: Nil
    val normal = Locations.kiev -> 3.0d :: Locations.newYork -> 4.0d :: Locations.zero -> 5.0d :: Nil

    val deviation = Manipulation.deviation(list, Manipulation.makeGrid(normal))

    cases.foreach { case ((x, y), t) => assert( deviation(x, y) === t) }
  }

}



@RunWith(classOf[JUnitRunner])
class ManipulationImplTest
  extends ManipulationTest with ExtractionTest

