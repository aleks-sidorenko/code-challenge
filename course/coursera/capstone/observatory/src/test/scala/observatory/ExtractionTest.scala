package observatory

import java.time.LocalDate

import org.scalactic.TolerantNumerics
import org.scalatest.FunSuite


trait ExtractionTest extends FunSuite {

  implicit val doubleEquality = TolerantNumerics.tolerantDoubleEquality(0.01)

  val year = 2015

  val stationsFile = "/stations.csv"
  val temperatureFile = s"/$year.csv"

  lazy val stations = Extraction.getStations(stationsFile).persist
  lazy val temperatures = Extraction.getTemperatures(temperatureFile, year).persist


  lazy val locateTemperatures = Extraction.locateTemperatures(year, stationsFile, temperatureFile)
  lazy val locateAverage = Extraction.locationYearlyAverageRecords(locateTemperatures)


  test("'getStations'") {
    stations.show()
    assert(stations.count() === 27708)
    assert(stations.filter((station: Station) => station.id.stn == "010000").count() === 0)
    assert(stations.filter((station: Station) => station.id.stn == "010014").count() === 1)


  }

  test("'getTemperatures'") {
    temperatures.show()
    assert(temperatures.count() === 10000)
    assert(temperatures.filter((temperature: Temperature) =>
      temperature.stationId.stn == "010250").count() === 365)
    assert(temperatures.filter((temperature: Temperature) =>
      temperature.stationId.stn == "010250" && temperature.date == Date(2015, 9, 29)).count() === 1)

  }


  test("'locateTemperatures'") {
    show(locateTemperatures)
    assert(locateTemperatures.count(_._1 == LocalDate.of(2015, 1, 13)) === 26)
    assert(locateTemperatures.count(_._2 == Location(78.25d, 22.817d)) === 292)
    assert(locateTemperatures.size === 8531)
  }

  test("'locationYearlyAverageRecords'") {
    show(locateAverage)
    assert(locateAverage.count(_._1 == Location(70.933, -8.667)) === 1)
    assert(locateAverage.size === 27)
  }

  private def show[T](it: Iterable[T], count: Int = 20) = it.take(count).foreach(println(_))


}