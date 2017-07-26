package observatory

import java.time.LocalDate



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

    r * Δσ(location1, location2)
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


final case class Color(red: Int, green: Int, blue: Int) {

  private val alpha = 0

  def argb: Int = ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | blue & 0xFF
}

