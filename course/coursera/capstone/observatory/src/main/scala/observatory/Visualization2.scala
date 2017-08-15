package observatory

import com.sksamuel.scrimage.{Image}
import observatory.Visualization.{interpolateColor}

/**
  * 5th milestone: value-added information visualization
  */
object Visualization2 {

  /**
    * @param x X coordinate between 0 and 1
    * @param y Y coordinate between 0 and 1
    * @param d00 Top-left value
    * @param d01 Bottom-left value
    * @param d10 Top-right value
    * @param d11 Bottom-right value
    * @return A guess of the value at (x, y) based on the four known values, using bilinear interpolation
    *         See https://en.wikipedia.org/wiki/Bilinear_interpolation#Unit_Square
    */
  def bilinearInterpolation(
    x: Double,
    y: Double,
    d00: Double,
    d01: Double,
    d10: Double,
    d11: Double
  ): Double = {
    Math.bilinearInterpolation(d00, d01, d10, d11)(x, y)
  }

  /**
    * @param grid Grid to visualize
    * @param colors Color scale to use
    * @param zoom Zoom level of the tile to visualize
    * @param x X value of the tile to visualize
    * @param y Y value of the tile to visualize
    * @return The image of the tile at (x, y, zoom) showing the grid using the given color scale
    */
  def visualizeGrid(
    grid: (Int, Int) => Double,
    colors: Iterable[(Double, Color)],
    zoom: Int,
    x: Int,
    y: Int
  ): Image = {

    def minMax(d: Double) = d.floor.toInt -> d.ceil.toInt

    val imageSize = ImageSize(256, 256)

    val tile = Tile(zoom, x, y)
    val tiles = tile.zoomIn(zoom + 8).zipWithIndex

    val pixels = tiles.par.map { case (tile, index) =>

      val location = tile.toLocation()

      val (lonMin, lonMax) = minMax(location.lon)
      val (latMin, latMax) = minMax(location.lat)

      val (d00, d01, d10, d11) = (grid(latMin, lonMin), grid(latMax, lonMin), grid(latMin, lonMax), grid(latMax, lonMax))

      val xDelta = location.lon - lonMin
      val yDelta = location.lat - latMin

      val temperature = bilinearInterpolation(xDelta, yDelta, d00, d01, d10, d11)

      val color = interpolateColor(colors, temperature)
      index -> color.pixel
    } .seq.toSeq
      .sortBy(_._1).map(_._2)

    Image(imageSize.width, imageSize.height, pixels.toArray)
  }

}
