package com.example.proof_of_concept.Api_Helper

import android.util.Log
import com.google.maps.android.PolyUtil
import org.osmdroid.util.GeoPoint

// RouteOption: routed-foot, routed-driving, routed-bike
suspend fun getRoutesFromOSRM(routeOption: String, locationStart: GeoPoint, locationEnd: GeoPoint): List<GeoPoint> {
    val OSRM = "https://routing.openstreetmap.de/${routeOption}/route/v1/foot/${locationStart.longitude},${locationStart.latitude};${locationEnd.longitude},${locationEnd.latitude}?overview=full&steps=true"
    val locations = mutableListOf<GeoPoint>()

    try {

        val jsonObject = sendRequest(OSRM)


        val routes = jsonObject.getJSONArray("routes")
        val geometryLineStep = routes.getJSONObject(0).getString("geometry")

        val decodeStep = PolyUtil.decode(geometryLineStep)
        decodeStep.forEach { locations.add(GeoPoint(it.latitude, it.longitude)) }

    } catch (e: Exception) {
        Log.e("getRoutesFromOSRM", "Failed to get route: ${e.message}")
    }

    return locations
}

