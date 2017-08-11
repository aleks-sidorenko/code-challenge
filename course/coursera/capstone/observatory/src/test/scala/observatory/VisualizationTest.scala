package observatory


import java.nio.file.Paths

import org.scalatest.FunSuite
import org.scalatest.prop.Checkers
import org.scalacheck.Gen
import org.scalacheck.Prop._



trait VisualizationTest extends FunSuite with Checkers {
  self: ExtractionTest =>


  val locationGen = for {
    lat <- Gen.choose(-90.0, 90.0)
    lon <- Gen.choose(-180.0, 180.0)
  } yield Location(lat, lon)

  val sampleGen = {
    val gen = for {
      loc <- locationGen
      value <- Gen.choose(-50.0, 50.0)
    } yield (loc, value)

    Gen.listOfN(10, gen)
  }

  val colourGen = for {
    red <- Gen.choose(0, 255)
    green <- Gen.choose(0, 255)
    blue <- Gen.choose(0, 255)
  } yield Color(red, green, blue)


  test("'predictTemperature' should predict temperatures in location close to stations") {
    val cases = List(
      Location(69.283d, 018.133d) -> -1.29d,
      Location(74.517d, 019.017d) -> 0.42d,
      Location(90.0d,-180.0d) -> -1.70d
    )

    cases.foreach { case (l, t) => assert(Visualization.predictTemperature(locateAverage, l) === t) }

  }

  test("'predictTemperature' should predict temperatures in location far from stations") {
    val cases = List(
      Locations.london -> 4.97d,
      Locations.kiev -> 2.51d
    )

    cases.foreach { case (l, t) => assert(Visualization.predictTemperature(locateAverage, l) === t) }

  }

  val colors = List(
    60d ->	Color(255, 255,	255),
    32d ->	Color(255, 0,	0),
    12d ->	Color(255, 255,	0),
    0d ->	  Color(0, 255, 255),
    -15d ->	Color(0, 0,	255),
    -27d ->	Color(255, 0,	255),
    -50d ->	Color(33,	0, 107),
    -60d -> Color(0,	0,	0)
  )

  test("'interpolateColor' should interpolate properly colors") {


    val cases = List(
      100d -> Color(255, 255,	255),
      18d -> Color(255, 179, 0),
      0d ->	  Color(0, 255, 255),
      1d -> Color(21, 255, 234),
      -100d -> Color(0, 0, 0)
    )

    cases.foreach { case (t, c) => assert(Visualization.interpolateColor(colors, t) === c) }

  }


  test("'interpolateColor' should interpolate properly random colors") {
    val gen = for {
      val1 <- Gen.choose(-50.0, 50.0)
      col1 <- colourGen
      val2 <- Gen.choose(-50.0, 50.0)
      col2 <- colourGen
      value <- Gen.choose(val1 min val2, val1 max val2)
    } yield (List((val1, col1), (val2, col2)), value)

    check(forAll(gen) {
      case (points: List[(Double, Color)], value: Double) => {
        val result = Visualization.interpolateColor(points, value)

        def checkComponent(c: (Color => Int)) = {
          c(result) >= points.map(p => c(p._2)).min &&
            c(result) <= points.map(p => c(p._2)).max
        }

        all(
          checkComponent(_.red),
          checkComponent(_.green),
          checkComponent(_.blue)
        )
      }
    })
  }


  test("'predictTemperature' & 'interpolateColor' should be correct") {
    val cases = List(
      Location(90.0d,-180.0d) ->  Color(0,226,255),
      Locations.zero ->  Color(35,255,220),
      Locations.kiev -> Color(53,255,202)
    )

    cases.foreach { case (l, c) =>
      val t = Visualization.predictTemperature(locateAverage, l)
      assert(Visualization.interpolateColor(colors, t) === c)
    }

  }

  test("'visualize' should create image") {

    val image = Visualization.visualize(locateAverage, colors)
    assert(image.width === 360)
    assert(image.height === 180)

    val file = Paths.get(Paths.get(getClass.getResource(stationsFile).toURI).getParent.toString, s"visualize-${year}.png")
    image.output(file)

    assert(file.toFile.exists())
  }


}
