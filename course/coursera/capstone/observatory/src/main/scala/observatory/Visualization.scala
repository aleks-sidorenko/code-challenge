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

    val distances = temperatures.map { case (l, t) => Location.distance(l, location) -> t }
    Math.inverseDistanceWeighted(distances, power = 3, minDistance = 1000)
  }


  /**
    * @param points Pairs containing a value and its associated color
    * @param value The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {
    val sorted = points.toArray.sortBy {case (t, _) => t }



    def interpolateColor(p0: (Double, Color), p1: (Double, Color), f: Color => Int)(t: Double): Int = {

      val prepare = (p: (Double, Color)) => p._1 -> f(p._2).toDouble

      val t0 = prepare(p0)
      val t1 = prepare(p1)

      math.round(Math.linearInterpolation(t0, t1)(t)).toInt
    }

    val index: Int = sorted.zipWithIndex.find { case ((t, _), _) => t > value } match {
      case Some(f) =>
        val (_, i) = f
        if (i > 0) i - 1 else i
      case None =>
        sorted.length - 2
    }

    val x0 = sorted(index)
    val x1 = sorted(index + 1)
    Color.withNormalization(
      interpolateColor(x0, x1, _.red)(value),
      interpolateColor(x0, x1, _.green)(value),
      interpolateColor(x0, x1, _.blue)(value)
    )

  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {
    val imageSize = ImageSize(360, 180)
    val pixels =
      for (location <- imageSize.locations()) yield {
      val temperature = predictTemperature(temperatures, location)
      val color = interpolateColor(colors, temperature)
      color.pixel
    }

    Image(imageSize.width, imageSize.height, pixels.toArray)

  }

}

