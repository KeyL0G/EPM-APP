package com.example.proof_of_concept.Api_Helper

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.proof_of_concept.R
import com.example.proof_of_concept.Viewmodels.ToiletDetail
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.internal.wait
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

suspend fun getStreet(location: GeoPoint):String {
    val OverPass = "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=${location.latitude}&lon=${location.longitude}"
    var fullAddress = ""
    sendRequest(OverPass) { jsonObject ->
        val address = jsonObject.getJSONObject("address")
        fullAddress+= "${address.getString("road")} ${address.getString("house_number")}"
    }.wait()

    return fullAddress
}

data class Toilet(val geoPoint: GeoPoint, val option: MutableList<String>)
suspend fun getMarkerOnLocation(mapView: MapView, context: Context, location: GeoPoint): List<Toilet>{
    val OverPass = "https://overpass-api.de/api/interpreter"
    val query = """
        [out:json];
        node["amenity"="toilets"](around:1000, ${location.latitude}, ${location.longitude});
        out body;
    """.trimIndent()
    val toilets = mutableListOf<Toilet>()

    sendRequestWithBody(OverPass, query, "text/plain; charset=utf-8".toMediaType()) { jsonObject ->
        val elements = jsonObject.getJSONArray("elements")

        for (i in 0 until elements.length()) {
            val element = elements.getJSONObject(i)
            val lat = element.optDouble("lat", 0.0)
            val lon = element.optDouble("lon", 0.0)
            val tag = element.getJSONObject("tags")

            val option: MutableList<String> = mutableListOf()
            if(tag.getString("fee") != null || tag.getString("fee") != "yes") option.add(tag.getString("fee"))
            if(tag.getString("changing_table") != null || tag.getString("changing_table") != "no") option.add(tag.getString("changing_table"))
            if (tag.getString("wheelchair") != null || tag.getString("wheelchair") != "no") option.add(tag.getString("wheelchair"))
            toilets.add(Toilet(GeoPoint(lat, lon), option))
        }

        for (location in toilets) {
            val marker = Marker(mapView)
            marker.position = location.geoPoint
            marker.icon = ContextCompat.getDrawable(context, R.drawable.location_blue)!!
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            mapView.overlays.add(marker)

            marker.setOnMarkerClickListener { markerClicked, map ->
                val position = markerClicked.position
                //osmdroid_viewmodel.setCurrentSelectedToilet(toilet)
                //onLocationClick()
                true
            }
        }

        mapView.invalidate()
    }.wait()

    return toilets
}
