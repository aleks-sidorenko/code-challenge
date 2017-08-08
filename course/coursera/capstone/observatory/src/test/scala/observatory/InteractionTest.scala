package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

import scala.collection.concurrent.TrieMap

trait InteractionTest extends FunSuite with Checkers {

  test("'tileLocation' should work") {

    val cases = List(
      (0, 0, 0) -> Location(85.05112877980659d,-180.0d),
      (2, 0, 0) -> Location(85.05112877980659d,-180.0d),
      (2, 2, 2) -> Location(0d, 0d),
      (10, 100, 300) -> Location(59.5343180010956d,-144.84375d)
    )

    cases.foreach { case ((zoom, x, y), l) =>
      assert(Interaction.tileLocation(zoom, x, y) === l)
    }

  }


}


@RunWith(classOf[JUnitRunner])
class InteractionImplTest
  extends InteractionTest

