import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import scala.collection.JavaConverters._

object ProcessScrappedData {
  private def processText(file: (String, String)) = {
    val fileName = file._1
    val fileText = file._2
    try {
      var doc: Document = Jsoup.parse(fileText, "utf-8")
      doc.select("script").remove()
      var imageTags: Elements = doc.getElementsByTag("img")
      val imageMap = imageTags.asScala.map { imageTag: Element =>
        var alt: String = imageTag.attr("alt")
        var src: String = imageTag.attr("src")
        var dataUrl: String = imageTag.attr("data-lazy")
        if(alt.nonEmpty && alt != "") {
          if(dataUrl.nonEmpty && dataUrl != "") {
            (dataUrl, alt)
          } else if(src.nonEmpty && src != "") {
            (src, alt)
          }
        }
      }

      if(imageMap.nonEmpty) {
        imageMap
      }
    } catch {
      case e: Exception => println("File IO exception")
    }
  }


  def main(args: Array[String]): Unit = {
    val conf  = new SparkConf().setAppName("Process_Scrapped_Data").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val inputFile = args(0)
    val outputFile = args(1)
    val filesRDD = sc.wholeTextFiles(inputFile)
    val imageMap = filesRDD.map{
      text => processText(text)
    }
    imageMap.saveAsTextFile("2.txt")


  }
}