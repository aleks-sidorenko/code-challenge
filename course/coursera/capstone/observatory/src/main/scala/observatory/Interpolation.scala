package observatory


object Interpolation {


  def linearInterpolation(p0: (Double, Double), p1: (Double, Double))(x: Double): Double = {
    val (x0, y0) = p0
    val (x1, y1) = p1

    val a = (y1 - y0) / (x1 - x0)
    val b = y0 - a * x0
    a * x + b
  }


  /**
    *
    *         @see https://en.wikipedia.org/wiki/Bilinear_interpolation#Unit_Square
    */

  def bilinearInterpolation(f00: Double, f01: Double, f10: Double, f11: Double)(x: Double, y: Double): Double = {
    f00 * (1 - x) * (1 - y) + f10 * x * (1 - y) + f01 * (1 - x) * y + f11 * x * y
  }

}

