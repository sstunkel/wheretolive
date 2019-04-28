name := "trafficdistancecalculator"

version := "0.1"

scalaVersion := "2.12.8"

resolvers += "jitpack" at "https://jitpack.io"
resolvers += Resolver.bintrayRepo("jroper", "maven")
libraryDependencies ++= Seq(
  "com.google.maps" % "google-maps-services" % "0.9.3",
  "com.github.jillesvangurp" % "geogeometry" % "v2.12",
  "org.slf4j" % "slf4j-simple" % "1.7.26",
  "org.typelevel"  %% "squants"  % "1.4.0",
  "com.typesafe.play" %% "play-json" % "2.7.2",
  "au.id.jazzy" %% "play-geojson" % "1.5.0",
  "com.github.nscala-time" %% "nscala-time" % "2.22.0",
  "org.scalactic" %% "scalactic" % "3.0.5" % "test",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)