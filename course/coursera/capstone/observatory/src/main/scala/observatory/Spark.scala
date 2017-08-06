package observatory

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object Spark {

  lazy val spark: SparkSession =
    SparkSession
      .builder()
      .appName("Observatory")
      .config("spark.master", "local")
      .getOrCreate()


  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
}