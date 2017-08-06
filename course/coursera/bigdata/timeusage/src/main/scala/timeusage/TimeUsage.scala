package timeusage

import java.nio.file.Paths

import org.apache.spark.sql._
import org.apache.spark.sql.execution.aggregate.TypedAverage
import org.apache.spark.sql.types._

/** Main class */
object TimeUsage {

  import org.apache.spark.sql.SparkSession
  import org.apache.spark.sql.functions._

  val spark: SparkSession =
    SparkSession
      .builder()
      .appName("Time Usage")
      .config("spark.master", "local")
      .getOrCreate()

  spark.sparkContext.setLogLevel("ERROR")



  // For implicit conversions like converting RDDs to DataFrames
  import spark.implicits._

  /** Main function */
  def main(args: Array[String]): Unit = {
    timeUsageByLifePeriod()
    timeUsageByLifePeriodSql()
    timeUsageByLifePeriodTyped()
  }

  object Activities {
    val primary = "primary"
    val working = "working"
    val leisure = "leisure"
    val unknown = "unknown"
  }


  object SummaryCollumns {
    val working = "working"
    val gender = "sex"
    val age = "age"

    val primaryActivities = "primaryNeeds"
    val workingActivities = "work"
    val leisureActivities = "other"
  }

  object Ages {
    val young = "young"
    val active = "active"
    val elder = "elder"
  }

  object Genders {
    val male = "male"
    val female = "female"
  }

  object Working {
    val yes = "working"
    val no = "not working"
  }

  def timeUsageByLifePeriod(): Unit = {
    val (columns, initDf) = read("/timeusage/atussum.csv")
    val (primaryNeedsColumns, workColumns, otherColumns) = classifiedColumns(columns)
    val summaryDf = timeUsageSummary(primaryNeedsColumns, workColumns, otherColumns, initDf)
    val finalDf = timeUsageGrouped(summaryDf)
    finalDf.show()
  }


  def timeUsageByLifePeriodSql(): Unit = {
    val (columns, initDf) = read("/timeusage/atussum.csv")
    val (primaryNeedsColumns, workColumns, otherColumns) = classifiedColumns(columns)
    val summaryDf = timeUsageSummary(primaryNeedsColumns, workColumns, otherColumns, initDf)
    val finalDf = timeUsageGroupedSql(summaryDf)
    finalDf.show()
  }

  def timeUsageByLifePeriodTyped(): Unit = {
    val (columns, initDf) = read("/timeusage/atussum.csv")
    val (primaryNeedsColumns, workColumns, otherColumns) = classifiedColumns(columns)
    val summaryDf = timeUsageSummaryTyped(timeUsageSummary(primaryNeedsColumns, workColumns, otherColumns, initDf))
    val finalDf = timeUsageGroupedTyped(summaryDf)
    finalDf.show()
  }

  /** @return The read DataFrame along with its column names. */
  def read(resource: String): (List[String], DataFrame) = {
    val rdd = spark.sparkContext.textFile(fsPath(resource))

    val headerColumns = rdd.first().split(",").to[List]
    // Compute the schema based on the first line of the CSV file
    val schema = dfSchema(headerColumns)

    val data =
      rdd
        .mapPartitionsWithIndex((i, it) => if (i == 0) it.drop(1) else it) // skip the header line
        .map(_.split(",").to[List])
        .map(row)

    val dataFrame =
      spark.createDataFrame(data, schema)

    (headerColumns, dataFrame)
  }

  /** @return The filesystem path of the given resource */
  def fsPath(resource: String): String =
    Paths.get(getClass.getResource(resource).toURI).toString

  /** @return The schema of the DataFrame, assuming that the first given column has type String and all the others
    *         have type Double. None of the fields are nullable.
    * @param columnNames Column names of the DataFrame
    */
  def dfSchema(columnNames: List[String]): StructType =
    StructType(StructField(columnNames.head, StringType, false) ::
      columnNames.tail.map(StructField(_, DoubleType, false)))


  /** @return An RDD Row compatible with the schema produced by `dfSchema`
    * @param line Raw fields
    */
  def row(line: List[String]): Row =
    Row(line.head :: line.tail.map(_.toDouble): _*)

  /** @return The initial data frame columns partitioned in three groups: primary needs (sleeping, eating, etc.),
    *         work and other (leisure activities)
    *
    * @see https://www.kaggle.com/bls/american-time-use-survey
    *
    * The dataset contains the daily time (in minutes) people spent in various activities. For instance, the column
    * “t010101” contains the time spent sleeping, the column “t110101” contains the time spent eating and drinking, etc.
    *
    * This method groups related columns together:
    * 1. “primary needs” activities (sleeping, eating, etc.). These are the columns starting with “t01”, “t03”, “t11”,
    *    “t1801” and “t1803”.
    * 2. working activities. These are the columns starting with “t05” and “t1805”.
    * 3. other activities (leisure). These are the columns starting with “t02”, “t04”, “t06”, “t07”, “t08”, “t09”,
    *    “t10”, “t12”, “t13”, “t14”, “t15”, “t16” and “t18” (those which are not part of the previous groups only).
    */
  def classifiedColumns(columnNames: List[String]): (List[Column], List[Column], List[Column]) = {


    def belongsTo(column: String, group: List[String]) = group.exists(column.startsWith(_))
    def isPrimary(column: String) = belongsTo(column, "t01" :: "t03" :: "t11" :: "t1801" :: "t1803" :: Nil)
    def isWorking(column: String) = belongsTo(column, "t05" :: "t1805" :: Nil)
    def isLeisure(column: String) = belongsTo(column, "t02" :: "t04" :: "t06" :: "t07" :: "t08" :: "t09" :: "t10" :: "t12" :: "t13" :: "t14" :: "t15" :: "t16" :: "t18" :: Nil)

    def group(column: String): String =
      if (isPrimary(column))
        Activities.primary
      else if (isWorking(column))
        Activities.working
      else if (isLeisure(column))
        Activities.leisure
      else
        Activities.unknown

    val grouped = columnNames.groupBy(group).mapValues(_.map(col(_)))

    (grouped.getOrElse(Activities.primary, Nil), grouped.getOrElse(Activities.working, Nil), grouped.getOrElse(Activities.leisure, Nil))
  }

  /** @return a projection of the initial DataFrame such that all columns containing hours spent on primary needs
    *         are summed together in a single column (and same for work and leisure). The “teage” column is also
    *         projected to three values: "young", "active", "elder".
    *
    * @param primaryNeedsColumns List of columns containing time spent on “primary needs”
    * @param workColumns List of columns containing time spent working
    * @param otherColumns List of columns containing time spent doing other activities
    * @param df DataFrame whose schema matches the given column lists
    *
    * This methods builds an intermediate DataFrame that sums up all the columns of each group of activity into
    * a single column.
    *
    * The resulting DataFrame should have the following columns:
    * - working: value computed from the “telfs” column of the given DataFrame:
    *   - "working" if 1 <= telfs < 3
    *   - "not working" otherwise
    * - sex: value computed from the “tesex” column of the given DataFrame:
    *   - "male" if tesex = 1, "female" otherwise
    * - age: value computed from the “teage” column of the given DataFrame:
    *   - "young" if 15 <= teage <= 22,
    *   - "active" if 23 <= teage <= 55,
    *   - "elder" otherwise
    * - primaryNeeds: sum of all the `primaryNeedsColumns`, in hours
    * - work: sum of all the `workColumns`, in hours
    * - other: sum of all the `otherColumns`, in hours
    *
    * Finally, the resulting DataFrame should exclude people that are not employable (ie telfs = 5).
    *
    * Note that the initial DataFrame contains time in ''minutes''. You have to convert it into ''hours''.
    */
  def timeUsageSummary(
    primaryNeedsColumns: List[Column],
    workColumns: List[Column],
    otherColumns: List[Column],
    df: DataFrame
  ): DataFrame = {
    val workingStatusProjection: Column = when('telfs >= 1.0d && 'telfs < 3.0d, Working.yes)
      .otherwise(Working.no).as(SummaryCollumns.working)

    val sexProjection: Column = when('tesex === 1.0d, Genders.male).otherwise(Genders.female)
      .as(SummaryCollumns.gender)

    val ageProjection: Column = when('teage.between(15.0d, 22.0d), Ages.young)
      .when('teage.between(23.0d, 55.0d), Ages.active)
      .otherwise(Ages.elder)
      .as(SummaryCollumns.age)

    def toHours(mins: Column) = mins / 60.0d

    val primaryNeedsProjection: Column = toHours(primaryNeedsColumns.reduce(_ + _))
      .as(SummaryCollumns.primaryActivities)
    val workProjection: Column = toHours(workColumns.reduce(_ + _))
      .as(SummaryCollumns.workingActivities)
    val otherProjection: Column = toHours(otherColumns.reduce(_ + _))
      .as(SummaryCollumns.leisureActivities)

    df
      .select(workingStatusProjection, sexProjection, ageProjection, primaryNeedsProjection, workProjection, otherProjection)
      .where($"telfs" <= 4) // Discard people who are not in labor force
  }

  /** @return the average daily time (in hours) spent in primary needs, working or leisure, grouped by the different
    *         ages of life (young, active or elder), sex and working status.
    * @param summed DataFrame returned by `timeUsageSumByClass`
    *
    * The resulting DataFrame should have the following columns:
    * - working: the “working” column of the `summed` DataFrame,
    * - sex: the “sex” column of the `summed` DataFrame,
    * - age: the “age” column of the `summed` DataFrame,
    * - primaryNeeds: the average value of the “primaryNeeds” columns of all the people that have the same working
    *   status, sex and age, rounded with a scale of 1 (using the `round` function),
    * - work: the average value of the “work” columns of all the people that have the same working status, sex
    *   and age, rounded with a scale of 1 (using the `round` function),
    * - other: the average value of the “other” columns all the people that have the same working status, sex and
    *   age, rounded with a scale of 1 (using the `round` function).
    *
    * Finally, the resulting DataFrame should be sorted by working status, sex and age.
    */
  def timeUsageGrouped(summed: DataFrame): DataFrame = {
    summed.groupBy(SummaryCollumns.working, SummaryCollumns.gender, SummaryCollumns.age)
      .agg(
        round(avg(SummaryCollumns.primaryActivities), 1).as(SummaryCollumns.primaryActivities),
        round(avg(SummaryCollumns.workingActivities), 1).as(SummaryCollumns.workingActivities),
        round(avg(SummaryCollumns.leisureActivities), 1).as(SummaryCollumns.leisureActivities)
      )
      .orderBy(SummaryCollumns.working, SummaryCollumns.gender, SummaryCollumns.age)

  }

  /**
    * @return Same as `timeUsageGrouped`, but using a plain SQL query instead
    * @param summed DataFrame returned by `timeUsageSumByClass`
    */
  def timeUsageGroupedSql(summed: DataFrame): DataFrame = {
    val viewName = s"summed"
    summed.createOrReplaceTempView(viewName)
    spark.sql(timeUsageGroupedSqlQuery(viewName))
  }

  /** @return SQL query equivalent to the transformation implemented in `timeUsageGrouped`
    * @param viewName Name of the SQL view to use
    */
  def timeUsageGroupedSqlQuery(viewName: String): String =
    s"SELECT working, sex, age, ROUND(AVG(primaryNeeds),1) as primaryNeeds, ROUND(AVG(work),1) as work, ROUND(AVG(other),1) as other FROM $viewName GROUP BY working, sex, age ORDER BY working, sex, age"

  /**
    * @return A `Dataset[TimeUsageRow]` from the “untyped” `DataFrame`
    * @param timeUsageSummaryDf `DataFrame` returned by the `timeUsageSummary` method
    *
    * Hint: you should use the `getAs` method of `Row` to look up columns and
    * cast them at the same time.
    */
  def timeUsageSummaryTyped(timeUsageSummaryDf: DataFrame): Dataset[TimeUsageRow] =
    timeUsageSummaryDf.as[TimeUsageRow]

  /**
    * @return Same as `timeUsageGrouped`, but using the typed API when possible
    * @param summed Dataset returned by the `timeUsageSummaryTyped` method
    *
    * Note that, though they have the same type (`Dataset[TimeUsageRow]`), the input
    * dataset contains one element per respondent, whereas the resulting dataset
    * contains one element per group (whose time spent on each activity kind has
    * been aggregated).
    *
    * Hint: you should use the `groupByKey` and `typed.avg` methods.
    */
  def timeUsageGroupedTyped(summed: Dataset[TimeUsageRow]): Dataset[TimeUsageRow] = {
    import org.apache.spark.sql.expressions.scalalang.typed._

    summed.groupByKey(t => (t.working, t.sex, t.age))
      .agg(
        round(avg[TimeUsageRow](_.primaryNeeds), 1).as[Double].name(SummaryCollumns.primaryActivities),
        round(avg[TimeUsageRow](_.work), 1).as[Double].name(SummaryCollumns.workingActivities),
        round(avg[TimeUsageRow](_.other), 1).as[Double].name(SummaryCollumns.leisureActivities))
      .map {
        case ((working, sex, age), primaryNeeds, work, other) =>
          TimeUsageRow(working, sex, age, primaryNeeds, work, other)
      }
      .orderBy(SummaryCollumns.working, SummaryCollumns.gender, SummaryCollumns.age)
  }
}

/**
  * Models a row of the summarized data set
  * @param working Working status (either "working" or "not working")
  * @param sex Sex (either "male" or "female")
  * @param age Age (either "young", "active" or "elder")
  * @param primaryNeeds Number of daily hours spent on primary needs
  * @param work Number of daily hours spent on work
  * @param other Number of daily hours spent on other activities
  */
case class TimeUsageRow(
  working: String,
  sex: String,
  age: String,
  primaryNeeds: Double,
  work: Double,
  other: Double
)