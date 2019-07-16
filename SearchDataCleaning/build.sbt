name := "SearchDataCleaning"

version := "0.1"

val sparkVersion = "2.4.3"  
scalaVersion := "2.11.7"

resolvers ++= Seq(
  "apache-snapshots" at "http://repository.apache.org/snapshots/"
)

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion
)