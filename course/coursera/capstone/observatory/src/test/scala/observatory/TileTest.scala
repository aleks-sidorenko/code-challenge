package observatory

import org.junit.runner.RunWith
import org.scalacheck.{Arbitrary, Gen}
import org.scalactic.TolerantNumerics
import org.scalatest.{FunSuite, ShouldMatchers}
import org.scalatest.junit.JUnitRunner
import org.scalacheck.Gen
import org.scalacheck.Prop._
import org.scalatest.prop.Checkers

import scala.collection.GenIterable


trait TileTest extends FunSuite with Checkers with ShouldMatchers {


  test("'toLocation' should work") {

    val cases = List(
      Tile(0, 0, 0) -> Location(85.05112877980659d,-180.0d),
      Tile(2, 0, 0) -> Location(85.05112877980659d,-180.0d),
      Tile(10, 10, 10) -> Location(84.7383871209534, -176.484375),
      Tile(2, 2, 2) -> Location(0d, 0d),
      Tile(10, 100, 300) -> Location(59.5343180010956d,-144.84375d)
    )

    cases.foreach { case (t, l) =>
      assert(t.location === l)
    }

  }

  test("tile must be consistent across zoom levels") {

    val zero = Tile(0, 0, 0)

    var zoomed = zero
    for {
      _ <- 1 to 12
    } {
      zoomed = zoomed.zoomInOnce().head
      assert(zoomed.x == zero.x && zoomed.y == zero.y)
    }
  }


  test("'zoomInOnce' should work") {

    val cases = List(
      Tile(0, 0, 0) -> List(Tile(1,0,0), Tile(1,1,0), Tile(1,0,1), Tile(1,1,1)),
      Tile(1, 0, 0) -> List(Tile(2,0,0), Tile(2,1,0), Tile(2,0,1), Tile(2,1,1)),
      Tile(2, 2, 2) -> List(Tile(3,4,4), Tile(3,5,4), Tile(3,4,5), Tile(3,5,5))
    )

    cases.foreach { case (t, zoomed) =>
      assert(t.zoomInOnce() === zoomed)
    }

  }

  test("'zoomIn' by 2 should work") {

    val cases = List(
      Tile(0, 0, 0) -> List(
        Tile(2,0,0), Tile(2,1,0), Tile(2,2,0), Tile(2,3,0),
        Tile(2,0,1), Tile(2,1,1), Tile(2,2,1), Tile(2,3,1),
        Tile(2,0,2), Tile(2,1,2), Tile(2,2,2), Tile(2,3,2),
        Tile(2,0,3), Tile(2,1,3), Tile(2,2,3), Tile(2,3,3))
    )

    cases.foreach { case (t, zoomed) =>
      assert(t.zoomIn(2) === zoomed)
    }

  }
}

@RunWith(classOf[JUnitRunner])
class TileImplTest
  extends TileTest

