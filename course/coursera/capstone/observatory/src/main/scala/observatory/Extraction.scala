package observatory

import java.nio.file.Paths
import java.time.LocalDate

import org.apache.spark.sql._
import org.apache.spark.sql.types._


/**
  * 1st milestone: data extraction
  */
object Extraction {

  import Spark._

  // For implicit conversions like converting RDDs to DataFrames
  import spark.implicits._


  private[observatory] def toCelcium(t: Column) = (t - 32.0d) * 5 / 9



  private def read(file: String, schema: StructType): DataFrame = {
    spark.read
      .schema(schema)
      .csv(fsPath(file))
  }

  final case class StationRow(stn: String, wban: String, lat: Double, lon: Double)

  final case class TemperatureRow(stn: String, wban: String, month: Int, day: Int, temperature: Double)


  private[observatory] def getStations(stationsFile: String): Dataset[Station] = {
    val schema = StructType(Seq(
      StructField("stn", StringType, false),
      StructField("wban", StringType, true),
      StructField("lat", DoubleType, true),
      StructField("lon", DoubleType, true))
    )

    read(stationsFile, schema)
      .filter($"lat".isNotNull && $"lon".isNotNull)
      .filter($"lat" =!= 0.0d && $"lon" =!= 0.0d)
      .map(r => Station(
        id = StationId(r.getAs[String]("stn"), r.getAs[String]("wban")),
        location = Location(r.getAs[Double]("lat"), r.getAs[Double]("lon"))))
  }


  private[observatory] def getTemperatures(temperaturesFile: String, year: Int): Dataset[Temperature] = {


    val schema = StructType(Seq(
      StructField("stn", StringType, false),
      StructField("wban", StringType, true),
      StructField("month", IntegerType, false),
      StructField("day", IntegerType, false),
      StructField("temperature", DoubleType, true))
    )

    read(temperaturesFile, schema)
      .na.fill(9999.9d, Seq("temperature"))
      .withColumn("temperature", toCelcium($"temperature"))
      .map(r => Temperature(
        stationId = StationId(r.getAs[String]("stn"), r.getAs[String]("wban")),
        date = Date(year = year, month = r.getAs[Int]("month"), day = r.getAs[Int]("day")),
        temperature = r.getAs[Double]("temperature")))
  }


  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Int, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Double)] = {
    val stations = getStations(stationsFile).persist()
    val temperatures = getTemperatures(temperaturesFile, year).persist()

    temperatures.join(stations, temperatures("stationId") === stations("id"))
      .select("date", "location", "temperature")
      .as[Join]
      .collect()
      .par.map(_.res).seq
  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] = {

    records.foldLeft[Map[Location, (Int, Double)]](Map.empty) { (map, r) =>
      r match {
        case (_, l, t) => {
          val (tc, tt) = map.getOrElse(l, (0, 0.0))
          map.updated(l, (tc + 1, tt + t))
        }
      }
    } .mapValues { case (c, t)  => t / c }
  }

  private def fsPath(resource: String): String =
    Paths.get(getClass.getResource(resource).toURI).toString

}