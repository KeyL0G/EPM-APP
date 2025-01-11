package com.example.proof_of_concept.Api_Helper

import com.google.maps.android.PolyUtil
import okhttp3.internal.wait
import org.osmdroid.util.GeoPoint

// RouteOption: routed-foot, routed-driving, routed-bike
fun getRoutesFromOSRM(routeOption: String, locationStart: GeoPoint, locationEnd: GeoPoint): List<GeoPoint> {
        val OSRM = "https://routing.openstreetmap.de/${routeOption}/route/v1/foot/${locationStart.longitude},${locationStart.latitude};${locationEnd.longitude},${locationEnd.latitude}?overview=full&steps=true"
        val locations = mutableListOf<GeoPoint>()

        sendRequest(OSRM){ jsonObject ->
            val routes = jsonObject.getJSONArray("routes")
            val geometryLineStep = routes.getJSONObject(0).getString("geometry")
            val decodeStep = PolyUtil.decode(geometryLineStep)
            decodeStep.forEach{ locations.add(GeoPoint(it.latitude, it.longitude)) }
        }.wait()

        return locations
}
