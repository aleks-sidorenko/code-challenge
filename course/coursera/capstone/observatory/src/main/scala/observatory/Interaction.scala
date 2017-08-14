package observatory

import java.util.concurrent.Executors

import com.sksamuel.scrimage.{Image, Pixel}
import observatory.Visualization.{interpolateColor, predictTemperature}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

/**
  * 3rd milestone: interactive visualization
  */
object Interaction {

  /**
    * @param zoom Zoom level
    * @param x X coordinate
    * @param y Y coordinate
    * @return The latitude and longitude of the top-left corner of the tile, as per http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
    */
  def tileLocation(zoom: Int, x: Int, y: Int): Location = {
    Tile(zoom, x, y).toLocation()
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @param zoom Zoom level
    * @param x X coordinate
    * @param y Y coordinate
    * @return A 256×256 image showing the contents of the tile defined by `x`, `y` and `zooms`
    */
  def tile(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)], zoom: Int, x: Int, y: Int): Image = {

    val tile = Tile(zoom, x, y)
    val locations = tile.zoomIn(zoom + 8).map(_.toLocation())

    val pixels = locations.map { case location =>
      val temperature = predictTemperature(temperatures, location)
      val color = interpolateColor(colors, temperature)
      color.pixel
    }

    Image(256, 256, pixels.toArray)
  }

  /**
    * Generates all the tiles for zoom levels 0 to 3 (included), for all the given years.
    * @param yearlyData Sequence of (year, data), where `data` is some data associated with
    *                   `year`. The type of `data` can be anything.
    * @param generateImage Function that generates an image given a year, a zoom level, the x and
    *                      y coordinates of the tile and the data to build the image from
    */
  def generateTiles[Data](
    yearlyData: Iterable[(Int, Data)],
    generateImage: (Int, Int, Int, Int, Data) => Unit
  ): Unit = {


    implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(8))

    val zeroTile = Tile(0, 0, 0)
    val years = yearlyData.par
    val tasks = for  {
      (year, data) <- years
      zoom <- 0 to 3
      t <- zeroTile.zoomIn(zoom)
    } yield Future { generateImage(year, zoom, t.x, t.y, data) }

    Await.result(Future.sequence(tasks.seq), Duration.Inf)
    ()
  }

}
