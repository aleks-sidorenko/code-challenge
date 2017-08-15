package observatory


object Math {


  def linearInterpolation(p0: (Double, Double), p1: (Double, Double))(x: Double): Double = {
    val (x0, y0) = p0
    val (x1, y1) = p1

    val a = (y1 - y0) / (x1 - x0)
    val b = y0 - a * x0
    a * x + b
  }


  /**
    *
    * @see https://en.wikipedia.org/wiki/Bilinear_interpolation#Unit_Square
    */

  def bilinearInterpolation(f00: Double, f01: Double, f10: Double, f11: Double)(x: Double, y: Double): Double = {
    f00 * (1 - x) * (1 - y) + f10 * x * (1 - y) + f01 * (1 - x) * y + f11 * x * y
  }


  /**
    *
    * @see https://en.wikipedia.org/wiki/Inverse_distance_weighting
    */
  def inverseDistanceWeighted(values: Iterable[(Double, Double)], power: Int, minDistance: Int): Double = {
    values.find { case (distance, _) => distance <= minDistance } match {
      case Some((_, temp)) => temp
      case None =>
        val (sum, weights) = values.aggregate((0.0, 0.0))(
          {
            case ((ws, iws), (distance, temp)) => {
              val w = 1.0d / math.pow(distance, power)
              (w * temp + ws, w + iws)
            }
          }, {
            case ((wsA, iwsA), (wsB, iwsB)) => (wsA + wsB, iwsA + iwsB)
          }
        )

        sum / weights
    }
  }

}

