package com.example.proof_of_concept.Api_Helper

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.proof_of_concept.R
import com.example.proof_of_concept.Viewmodels.ToiletDetail
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.internal.wait
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.jsoup.Jsoup


suspend fun getStreet(location: GeoPoint): String {
    val overPassUrl =
        "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=${location.latitude}&lon=${location.longitude}"

    return try {
        val jsonResponse = sendRequest(overPassUrl)
        val addressObject = jsonResponse.getJSONObject("address")
        val road = addressObject.optString("road", "")
        val houseNumber = addressObject.optString("house_number", "")
        "${road} ${houseNumber}".trim()
    } catch (e: Exception) {
        Log.e("getStreet", "Failed to get street information: ${e.message}")
        "Unknown Address"
    }
}

enum class ToiletOptions{
    FEE,
    WHEELCHAIR,
    CHANGING_TABLE,
    UNKNOWN
}


data class Toilet(val geoPoint: GeoPoint, val option: MutableList<ToiletOptions>)
suspend fun getMarkerOnLocation(mapView: MapView, context: Context, location: GeoPoint): List<Toilet> {
    val overPassUrl = "https://overpass-api.de/api/interpreter"
    val query = """
        [out:json];
        node["amenity"="toilets"](around:1000, ${location.latitude}, ${location.longitude});
        out body;
    """.trimIndent()

    val toilets = mutableListOf<Toilet>()


    try {
        val jsonObject = sendRequestWithBody(overPassUrl, query, "text/plain; charset=utf-8".toMediaType())
        val elements = jsonObject.getJSONArray("elements")

        for (i in 0 until elements.length()) {
            val element = elements.getJSONObject(i)
            val lat = element.optDouble("lat", 0.0)
            val lon = element.optDouble("lon", 0.0)

    

            val tag = element.optJSONObject("tags")
            val fee = tag?.optString("fee", "unknown") ?: "unknown"
            val changingTable = tag?.optString("changing_table", "unknown") ?: "unknown"
            val wheelchair = tag?.optString("wheelchair", "unknown") ?: "unknown"


            val options: MutableList<ToiletOptions> = mutableListOf(
                if (fee == "unknown") ToiletOptions.UNKNOWN else ToiletOptions.FEE,
                if (changingTable == "unknown") ToiletOptions.UNKNOWN else ToiletOptions.CHANGING_TABLE,
                if (wheelchair == "unknown") ToiletOptions.UNKNOWN else ToiletOptions.WHEELCHAIR,
            )
            toilets.add(Toilet(GeoPoint(lat, lon), options))
        }

        toilets.forEach { toilet ->
            val marker = Marker(mapView)
            marker.position = toilet.geoPoint
            marker.icon = ContextCompat.getDrawable(context, R.drawable.location_blue)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            mapView.overlays.add(marker)

            marker.setOnMarkerClickListener { markerClicked, _ ->
                val position = markerClicked.position
                Log.d("MarkerClicked", "Marker clicked at: $position")
                true
            }
        }

        mapView.invalidate()
    } catch (e: Exception) {
        Log.e("getMarkerOnLocation", "Failed to fetch markers: ${e.message}")
    }

    return toilets
}

fun buildOverpassQuery(location: GeoPoint, filterFree: Boolean, filterChangingTable: Boolean, filterWheelchair: Boolean): String {
    val filters = mutableListOf<String>()

    if (filterFree) filters.add("""["fee"!="yes"]""")
    if (filterChangingTable) filters.add("""["changing_table"="yes"]""")
    if (filterWheelchair) filters.add("""["wheelchair"="yes"]""")

    val filterString = filters.joinToString(" ")

    return """
        [out:json];
        node["amenity"="toilets"](around:1000, ${location.latitude}, ${location.longitude}) $filterString;
        out body;
    """.trimIndent()
}

