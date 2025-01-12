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
    val overPassUrl = "https://nominatim.openstreetmap.org/reverse?format=html&lat=${location.latitude}&lon=${location.longitude}"

    return try {
        val htmlContent = sendRequestWithHTML(overPassUrl)
        val document = Jsoup.parse(htmlContent)
        val addressElement = document.select("div#address")
        val road = addressElement.select("span.road").text()
        val houseNumber = addressElement.select("span.house_number").text()
        "${road} ${houseNumber}".trim()
    } catch (e: Exception) {
        Log.e("getStreet", "Failed to get street information: ${e.message}")
        "Unknown Address"
    }
}



data class Toilet(val geoPoint: GeoPoint, val option: MutableList<String>)
suspend fun getMarkerOnLocation(mapView: MapView, context: Context, location: GeoPoint): List<Toilet> {
    val OverPass = "https://overpass-api.de/api/interpreter"
    val query = """
        [out:json];
        node["amenity"="toilets"](around:1000, ${location.latitude}, ${location.longitude});
        out body;
    """.trimIndent()
    val toilets = mutableListOf<Toilet>()

    val jsonObject = sendRequestWithBody(OverPass, query, "text/plain; charset=utf-8".toMediaType())
    val elements = jsonObject.getJSONArray("elements")

    for (i in 0 until elements.length()) {
        val element = elements.getJSONObject(i)
        val lat = element.optDouble("lat", 0.0)
        val lon = element.optDouble("lon", 0.0)
        val tag = element.getJSONObject("tags")
        val option: MutableList<String> = mutableListOf()
        if (tag.getString("fee") != null || tag.getString("fee") != "yes") option.add(tag.getString("fee"))
        toilets.add(Toilet(GeoPoint(lat, lon), option))
        Log.e("Lat", "${toilets[0]}")
    }

    // Marker setzen
    for (location in toilets) {
        val marker = Marker(mapView)
        marker.position = location.geoPoint
        marker.icon = ContextCompat.getDrawable(context, R.drawable.location_blue)!!
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        mapView.overlays.add(marker)

        marker.setOnMarkerClickListener { markerClicked, map ->
            val position = markerClicked.position
            true
        }
    }

    mapView.invalidate()
    return toilets
}

