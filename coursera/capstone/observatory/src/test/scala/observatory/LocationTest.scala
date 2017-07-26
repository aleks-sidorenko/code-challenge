package observatory

import org.junit.runner.RunWith
import org.scalactic.TolerantNumerics
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

trait LocationTest extends FunSuite with Checkers {


  test("'distance' should work") {
    val precision = 0.01d

    val cases = List(
      (Location(50.43, 30.52), Location(46.4666666667, 30.7333333333), 440800),
      (Location(-46.466667, -30.733333), Location(46.4666666667, 30.7333333333), 14585730),
      (Location(50.43, 30.52), Location(52.520385, 13.4090013), 1202760 ))

    cases.foreach { case (l1, l2, d) => assert(math.abs(Location.distance(l1, l2).toLong - d) / d <= precision) }

  }


  test("'apply' should create location from coordinates") {


    val cases = List(
      (0, 0) -> Location(90.0d, -180.0d),
      (180, 90) -> Location(0.0d, 0.0d),
      (360, 180) -> Location(-90.0d, 180.0d),
      (360, 0) -> Location(90.0d, 180.0d)
    )

    cases.foreach { case ((w, h), l) => assert(Location(w, h, 360, 180) === l) }

  }
}

@RunWith(classOf[JUnitRunner])
class LocationImplTest
  extends LocationTest

