package com.oakheartlabs.wheretolive

import au.id.jazzy.play.geojson
import au.id.jazzy.play.geojson.{Feature, FeatureCollection, LatLng, Point}
import com.jillesvangurp.geo.GeoGeometry
import play.api.libs.json.Json
import squants.space.{Length, Meters, UsMiles}

class GeoWebGenerator(gms: GoogleMapsService) {
  def generateWeb(startingAddress: String): Unit = {
      val initialLatLong = gms.geocodeAddress(startingAddress)
      println(s"initial lat long - $initialLatLong")
      val numberOfIterations = 2
      val increment = 5
      val initialPolySides = 5
      val polyIncreaseFactor = 1
      val allPolys:IndexedSeq[Feature[LatLng]] = (1 to numberOfIterations).flatMap(index => {
        val polySides:Int = (initialPolySides + (initialPolySides * (polyIncreaseFactor * (index - 1)))).toInt
        val poly = circle2Polygon(polySides, initialLatLong, UsMiles(increment * index))
        poly
            .flatMap(latlng => {
              gms.reverseGeocodeAddress(latlng)
            })
          .map(reverseGeocodeResult => {
            val drivingDuration = gms.getTimeToDestination(
              latLngToGRequestString(initialLatLong),
              latLngToGRequestString(reverseGeocodeResult.latLng)
            )

            Feature(
              Point(reverseGeocodeResult.latLng),
              properties = Some(Json.obj(
                "address" -> reverseGeocodeResult.formattedAddress,
                "drivingDuration" -> drivingDuration
              )))
          })
      })

      val firstPoint:Feature[LatLng] = Feature(Point(initialLatLong), properties = Some(Json.obj()))
      val test:scala.collection.immutable.Seq[Feature[LatLng]] = Seq(firstPoint).toIndexedSeq ++ allPolys
      val allFeatures = FeatureCollection[LatLng](test)
      println(Json.toJson(allFeatures).toString)
    }

  private def circle2Polygon(segments: Int, latLng: LatLng, radius: Length):Seq[LatLng] = {
    GeoGeometry.circle2polygon(segments, latLng.lat, latLng.lng, radius.toMeters)
      .toSeq
      .map(lngLatArray => LatLng(lngLatArray(1), lngLatArray(0)))
  }

  private def latLngToGRequestString(latLng: LatLng): String = {
    s"${latLng.lat},${latLng.lng}"
  }

}
