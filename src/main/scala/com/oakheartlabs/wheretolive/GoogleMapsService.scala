package com.oakheartlabs.wheretolive

import java.time.Instant

import au.id.jazzy.play.geojson.LatLng
import com.google.gson.GsonBuilder
import com.google.maps.model.{LatLng => GLatLng}
import com.google.maps.{DirectionsApi, GeoApiContext, GeocodingApi}
import squants.time.{Seconds, Time}

case class ReverseGeocodeResult(formattedAddress: String, latLng: LatLng)

class GoogleMapsService {
  private val apiKey = ""
  private val client = new GeoApiContext.Builder()
    .apiKey(apiKey)
    .build()

  private val prettyPrinter = new GsonBuilder().setPrettyPrinting().create()

  def geocodeAddress(address: String): LatLng = {
    val result = GeocodingApi.geocode(client, address).await().toSeq
    println(prettyPrinter.toJson(result))
    result.head.geometry.location
  }

  def reverseGeocodeAddress(latLng: LatLng):Option[ReverseGeocodeResult]  = {
    val result = GeocodingApi.reverseGeocode(client, latLng).await.toSeq
    println(prettyPrinter.toJson(result))
    result.headOption.map(item => ReverseGeocodeResult(item.formattedAddress, item.geometry.location))
  }

  def getTimeToDestination(origin: String, destination: String): Time = {
    val result = DirectionsApi
      .getDirections(client, origin, destination)
//      .arrivalTime(Instant.now())
      .departureTime(Instant.now())
      .await
    println(prettyPrinter.toJson(result))
    val duration = Seconds(result
      .routes
      .head
      .legs
      .map(_.durationInTraffic.inSeconds)
      .sum)
    println(s"duration - ${duration.toMinutes}")
    duration
  }

  implicit def gLatLngToLatLng(gLatLng: com.google.maps.model.LatLng): LatLng = {
    LatLng(gLatLng.lat, gLatLng.lng)
  }

  implicit def latLngToGLatLng(latLng: LatLng): GLatLng = {
    new GLatLng(latLng.lat, latLng.lng)
  }



}
