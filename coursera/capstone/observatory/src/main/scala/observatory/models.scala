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

final case class Location(lat: Double, lon: Double)

final case class Color(red: Int, green: Int, blue: Int)

