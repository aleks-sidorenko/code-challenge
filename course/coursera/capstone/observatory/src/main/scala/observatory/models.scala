package observatory

import java.time.LocalDate

import com.sksamuel.scrimage.RGBColor

import scala.annotation.tailrec
import scala.collection.{GenIterable, GenSeqLike}
import scala.math._



case class Join(date: Date, location: Location, temperature: Double) {
  def res = (date.toLocalDate(), location, temperature)
}

object Date {
  def apply(date: LocalDate) = new Date(date.getYear, date.getMonth.getValue, date.getDayOfMonth)
}
final case class Date(year: Int, month: Int, day: Int) {
  def toLocalDate() = LocalDate.of(year, month, day)
}

final case class StationId(stn: String, wban: String)

final case class Station(id: StationId, location: Location)

final case class Temperature(stationId: StationId, date: Date, temperature: Double)

object Location {

  val earthRadius = 6371000

  def distance(location1: Location, location2: Location): Double = {
    math.abs(earthRadius * Δσ(location1, location2))
  }

  def apply(width: Int, height: Int, maxWidth: Int, maxHeight: Int): Location = {
    val widthRadio = 360 / maxWidth.toDouble
    val heightRatio = 180 / maxHeight.toDouble

    Location(90 - (height * heightRatio), (width * widthRadio) - 180)
  }


  private def Δλ(location1: Location, location2: Location) = location1.λ - location2.λ

  private def Δσ(location1: Location, location2: Location): Double = {
    acos(sin(location1.φ) * sin(location2.φ) + cos(location1.φ) * cos(location2.φ) * cos (Δλ(location1, location2)))
  }

}

final case class Location(lat: Double, lon: Double) {

  def φ = toRadians(lat)
  def λ = toRadians(lon)

}

final case class ImageSize(width: Int, height: Int) {

  final case class ImageCell(x: Int, y: Int) {

    def toLocation(): Location = Location(height.toDouble / 2 - y, x - width.toDouble / 2)

  }

  def cells(): Iterable[ImageCell] = {
    for {
      y <- 0 until height
      x <- 0 until width
    } yield ImageCell(x, y)
  }

  def locations() = cells().map(_.toLocation())

}

object Color {
  private def normalize(component: Int) = {
    if (component < 0) 0
    else if (component > 255) 255
    else component
  }

  def withNormalization(red: Int, green: Int, blue: Int) = Color(normalize(red), normalize(green), normalize(blue))

}

final case class Color(red: Int, green: Int, blue: Int) {
  require(0 <= red && red <= 255, "Red component is invalid")
  require(0 <= green && green <= 255, "Green component is invalid")
  require(0 <= blue && blue <= 255, "Blue component is invalid")

  private val alpha = 127

  def pixel = RGBColor(red, green, blue, alpha).toPixel
}


final case class Tile(zoom: Int, x: Int, y: Int) {

  lazy val location = toLocation()

  val n = pow2(zoom)

  require(x >= 0 && x <= n - 1)
  require(y >= 0 && y <= n - 1)

  @inline
  private def pow2(x: Int) = 1 << x

  def zoomIn(z: Int): Iterable[Tile] = {
    require(z >= 0)

    val n0 = pow2(z)

    val x0 = pow2(z) * x
    val y0 = pow2(z) * y

    for {
      i <- 0 until n0
      j <- 0 until n0
    } yield Tile(zoom + z, x0 + j, y0 + i)
  }

  def zoomInOnce() = {
    zoomIn(1)
  }

  def uri = new java.net.URI("http://tile.openstreetmap.org/" + zoom + "/" + x + "/" + y + ".png")

  def toLocation(): Location = {


    val lat = toDegrees(atan(sinh(Pi * (1.0d - 2.0d * y / n))))
    val lon = 360.0d * x / n - 180.0d

    Location(lat, lon)

  }

}



object LocationGrid {

  type GridFunc = ((Int, Int) => Double)

  def average(grids: Iterable[GridFunc]): GridFunc  = {
    (lat, lon) => {
      val temps = grids.map(grid => grid(lat, lon))
      temps.sum / temps.size
    }
  }



}

final case class LocationGrid(temperatures: Iterable[(Location, Double)]) extends LocationGrid.GridFunc {

  import LocationGrid._

  lazy val grid = (for {
    lat <- -89 to 90
    lon <- -180 to 179
  } yield {
    val location = Location(lat, lon)
    location -> Visualization.predictTemperature(temperatures, location)
  }).toMap

  override def apply(lat: Int, lon: Int): Double = grid(Location(lat, lon))

  def deviation(normals: GridFunc): GridFunc = {
    (lat, lon) => this(lat, lon) - normals(lat, lon)
  }

}
