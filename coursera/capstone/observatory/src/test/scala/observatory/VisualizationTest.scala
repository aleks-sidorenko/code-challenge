package observatory


import java.io.File
import java.nio.file.Paths

import observatory.Extraction.getClass
import org.scalatest.FunSuite
import org.scalatest.prop.Checkers

trait VisualizationTest extends FunSuite with Checkers {
  self: ExtractionTest =>

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
      Location(71.250d, 18.5d) -> 2.79d,
      Location(79.05d, 15.25d) -> -1.60d
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


  test("'predictTemperature' & 'interpolateColor' should be correct") {
    val cases = List(
      Location(90.0d,-180.0d) ->  Color(255,0,0)
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
