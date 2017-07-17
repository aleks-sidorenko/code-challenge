package timeusage

import org.apache.spark.sql.{ColumnName, DataFrame, Row}
import org.apache.spark.sql.types.{
  DoubleType,
  StringType,
  StructField,
  StructType
}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, FunSuite}

import scala.util.Random

@RunWith(classOf[JUnitRunner])
class TimeUsageSuite extends FunSuite with BeforeAndAfterAll {


  lazy val timeUsage = TimeUsage


  lazy val (columns, initDf) = timeUsage.read("/timeusage/atussum.csv")
  lazy val (primaryNeedsColumns, workColumns, otherColumns) = timeUsage.classifiedColumns(columns)
  lazy val summaryDf = timeUsage.timeUsageSummary(primaryNeedsColumns, workColumns, otherColumns, initDf)
  lazy val finalDf = timeUsage.timeUsageGrouped(summaryDf)

  lazy val sqlDf = timeUsage.timeUsageGroupedSql(summaryDf)
  lazy val summaryDs = timeUsage.timeUsageSummaryTyped(summaryDf)
  lazy val finalDs = timeUsage.timeUsageGroupedTyped(summaryDs)

  test("'row' should work") {

    val line = List("foo", "1", "2")

    val res = TimeUsage.row(line)

    assert(res.length == 3)
    assert(res.toSeq == Seq("foo", 1.0, 2.0))

  }

  test("'classifiedColumns' should work on dataset") {

    val (primary, working, leisure) = TimeUsage.classifiedColumns(columns)
    assert(primary.length == 55)
    assert(working.length == 23)
    assert(leisure.length == 346)
  }

  test("'timeUsageSummary' should work on dataset"){
    summaryDf.show()
    assert(summaryDf.columns.length === 6)
    assert(summaryDf.count === 114997)
  }


  test("'timeUsageGrouped' should work on dataset"){
    finalDf.show()
    assert(finalDf.count === 2*2*3)
    assert(finalDf.head.getDouble(3) === 12.4)
    assert(finalDf.head.getDouble(4) === 0.5)
    assert(finalDf.head.getDouble(5) === 10.8)
  }

  test("'timeUsageGroupedSql' should work on dataset"){
    sqlDf.show()
    assert(sqlDf.count === 2*2*3)
    assert(sqlDf.head.getDouble(3) === 12.4)
    assert(sqlDf.head.getDouble(4) === 0.5)
    assert(sqlDf.head.getDouble(5) === 10.8)
  }

  test("'timeUsageSummaryTyped' should work on dataset") {
    summaryDs.show()
    assert(summaryDs.head.other === 8.75)
    assert(summaryDs.count === 114997)
  }

  test("'timeUsageGroupedTyped' should work on dataset"){
    finalDs.show()
    assert(finalDs.count === 2*2*3)
    assert(finalDs.head.primaryNeeds === 12.4)
    assert(finalDs.head.work === 0.5)
    assert(finalDs.head.other === 10.8)
  }

}
