package observatory

import com.sksamuel.scrimage.{Image, Pixel}

import scala.collection.GenIterable

/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location Location where to predict the temperature
    * @see https://en.wikipedia.org/wiki/Inverse_distance_weighting
    * @return The predicted temperature at `location`
    *
    */
  def predictTemperature(temperatures: Iterable[(Location, Double)], location: Location): Double = {


    def idw(pairs: GenIterable[(Double, Double)], power: Int): Double = {
      val (ws, iws) = pairs
        .aggregate((0.0, 0.0))(
          {
            case ((ws, iws), (x, y)) => {
              val w = 1 / math.pow(x, power)
              (w * y + ws, w + iws)
            }
          }, {
            case ((ws1, iws1), (ws2, iws2)) => (ws1 + ws2, iws1 + iws2)
          }
        )

      ws / iws
    }

    val distances = temperatures.par.map { case (l, t) => Location.distance(l, location) -> t }

    val minDistance = 1000

    val (distance, temperature) = distances.minBy { case (d, _) => d }

    if (distance <= minDistance)
      temperature
    else
      idw(distances, power = 2)
  }


  /**
    * @param points Pairs containing a value and its associated color
    * @param value The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {
    val sorted = points.toArray.sortBy {case (t, _) => t }


    def li(p0: (Double, Double), p1: (Double, Double))(x: Double): Double = {
      val (x0, y0) = p0
      val (x1, y1) = p1

      val a = (y1 - y0) / (x1 - x0)
      val b = y0 - a * x0
      a * x + b
    }

    def interpolateColor(p0: (Double, Color), p1: (Double, Color), f: Color => Int)(t: Double): Int = {

      val prepare = (p: (Double, Color)) => p._1 -> f(p._2).toDouble

      val t0 = prepare(p0)
      val t1 = prepare(p1)

      math.round(li(t0, t1)(t)).toInt
    }

    sorted.zipWithIndex.find { case ((t, _), _) => t > value } match {
      case Some(f) =>
        val ((_, c), i) = f
        if (i == 0)
          c
        else {
          val x0 = sorted(i - 1)
          val x1 = sorted(i)
          Color(
            interpolateColor(x0, x1, _.red)(value),
            interpolateColor(x0, x1, _.green)(value),
            interpolateColor(x0, x1, _.blue)(value)
          )
        }
      case None =>
        val (_, c) = sorted.last
        c
    }

  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {

    val pixels = for {
      h <- (0 until 180).par
      w <- 0 until 360
    } yield {
      val location = Location(w, h)
      val temperature = predictTemperature(temperatures, location)
      val color = interpolateColor(colors, temperature)
      Pixel(color.argb)
    }

    Image(360, 180, pixels.toArray)

  }

}

