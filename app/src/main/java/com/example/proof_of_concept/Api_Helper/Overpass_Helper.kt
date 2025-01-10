package com.example.proof_of_concept.Api_Helper

import android.content.Context
import androidx.core.content.ContextCompat
import okhttp3.MediaType.Companion.toMediaType
import org.osmdroid.library.R
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

fun getMarkerOnLocation(mapView: MapView, context: Context, location: GeoPoint) {
    val OverPass = "https://overpass-api.de/api/interpreter"
    val query = """
        [out:json];
        node["amenity"="toilets"](around:1000, ${location.latitude}, ${location.longitude});
        out body;
    """.trimIndent()

    sendRequestWithBody(OverPass, query, "text/plain; charset=utf-8".toMediaType()) { jsonObject ->
        val locations = mutableListOf<GeoPoint>()
        val elements = jsonObject.getJSONArray("elements")

        for (i in 0 until elements.length()) {
            val element = elements.getJSONObject(i)
            val lat = element.optDouble("lat", 0.0)
            val lon = element.optDouble("lon", 0.0)
            locations.add(GeoPoint(lat, lon))
        }

        for (location in locations) {
            val marker = Marker(mapView)
            marker.position = location
            marker.icon = ContextCompat.getDrawable(context, R.drawable.marker_default_focused_base)!!
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            mapView.overlays.add(marker)

            marker.setOnMarkerClickListener { markerClicked, map ->
                // Hier kannst du markerClicked.position verwenden
                true
            }
        }

        mapView.invalidate()
    }
}
