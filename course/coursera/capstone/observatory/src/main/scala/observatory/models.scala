package observatory

import java.time.LocalDate

import com.sksamuel.scrimage.RGBColor



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
  def distance(location1: Location, location2: Location): Double = {
    val r = 6371000 // radius of Earth in m

    math.abs(r * Δσ(location1, location2))
  }

  def apply(width: Int, height: Int, maxWidth: Int, maxHeight: Int): Location = {
    val widthRadio = 360 / maxWidth.toDouble
    val heightRatio = 180 / maxHeight.toDouble

    Location(90 - (height * heightRatio), (width * widthRadio) - 180)
  }

  private def Δλ(location1: Location, location2: Location) = location1.λ - location2.λ

  private def Δσ(location1: Location, location2: Location): Double = {
    import math._
    acos(sin(location1.φ) * sin(location2.φ) + cos(location1.φ) * cos(location2.φ) * cos (Δλ(location1, location2)))
  }

}

final case class Location(lat: Double, lon: Double) {


  private def radians(v: Double) = v * math.Pi / 180

  def φ = radians(lat)
  def λ = radians(lon)

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


  def pixel = RGBColor(red, green, blue, 0).toPixel
}

