package observatory

import org.junit.runner.RunWith
import org.scalactic.TolerantNumerics
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers



object Locations {

  val berlin = Location(52.520385, 13.4090013)
  val london = Location(51.508530, -0.076132)

  val paris = Location(48.864716, 2.349014)
  val kiev = Location(50.45466d, 30.5238d)
  val odessa = Location(46.4666666667d, 30.7333333333d)
  val newYork = Location(40.730610d, -73.935242d)

  val zero = Location(0d, 0d)

}


trait LocationTest extends FunSuite with Checkers {


  test("'distance' should work") {
    val precision = 0.01d

    val cases = List(
      (Locations.kiev, Locations.odessa, 440800),
      (Locations.london, Locations.paris, 344000),
      (Locations.london, Locations.newYork, 5585000),

      (Location(50.43, 30.52), Locations.odessa, 1202760 ),
      (Location(90.0d,-180.0d), Location(-90.0d, -180.0d), Location.earthRadius * 2),
      (Location(-90.0d,-180.0d), Location(90.0d, -180.0d), Location.earthRadius * 2),
      (Location(90.0d, 180.0d), Location(90.0d, -180.0d), 0),
      (Location(90.0d, -180.0d), Location(90.0d, -180.0d), 0)
    )

    cases.foreach { case (l1, l2, d) =>
      if (d > 0) assert(math.abs(Location.distance(l1, l2).toLong - d) / d <= precision)
      else assert(math.abs(Location.distance(l1, l2).toLong - d) <= precision)
    }

  }


  test("'apply' should create location from coordinates") {


    val cases = List(
      (0, 0) -> Location(90.0d, -180.0d),
      (180, 90) -> Locations.zero,
      (360, 180) -> Location(-90.0d, 180.0d),
      (360, 0) -> Location(90.0d, 180.0d)
    )

    cases.foreach { case ((w, h), l) => assert(Location(w, h, 360, 180) === l) }

  }
}

@RunWith(classOf[JUnitRunner])
class LocationImplTest
  extends LocationTest

