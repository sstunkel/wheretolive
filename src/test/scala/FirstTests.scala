import au.id.jazzy.play.geojson.LatLng
import org.scalatest.FunSuite
import com.oakheartlabs.wheretolive.{GeoWebGenerator, GoogleMapsService}

class FirstTests extends FunSuite {
  val service = new GoogleMapsService()
  val geoWebGenerator = new GeoWebGenerator(service)

  test("geocode test") {
    val result = service.geocodeAddress("12 Heritage Way, Marblehead, MA")
    println(result)
    assertResult(42.50062720) {
      result.lat
    }
    assertResult(-70.86135030) {
      result.lng
    }
  }

  test("reverse geocode test") {
      val result = service.reverseGeocodeAddress(LatLng(42.50062720, -70.86135030))
      println(result)
//    val service = new GoogleMapsService()
//    val result = service.reverseGeocodeAddress()
//    println(result)
  }

  test("reverse geocode water") {
    val result = service.reverseGeocodeAddress(LatLng(42.38504955243599, -70.89305877685547))
  }

  test("reverse geocode park") {
    val result = service.reverseGeocodeAddress(LatLng(42.44759143280502, -71.08523368835448))
  }

  test("route duration") {
    val result = service.getTimeToDestination("12 Heritage Way, Marblehead, MA", "940 Winter Stree, Waltham, MA")
  }

  test("geoweb") {
    geoWebGenerator.generateWeb("940 Winter St. Waltham, MA")
  }
}
