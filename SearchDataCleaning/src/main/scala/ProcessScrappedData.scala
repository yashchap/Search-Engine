import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}

object ProcessScrappedData {
  def main(args: Array[String]): Unit = {
    val conf  = new SparkConf().setAppName("Process_Scrapped_Data").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val inputFile = args(0)
    val outputFile = args(1)
    val output = sc.textFile(inputFile).map{
      text => text.replace("[\\r\\n]", "")
        text.replace(" +", "")
    }
    output.saveAsTextFile(outputFile)
  }
}