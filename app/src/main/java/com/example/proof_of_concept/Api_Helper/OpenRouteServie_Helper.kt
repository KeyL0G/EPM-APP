package com.example.proof_of_concept.Api_Helper

import android.util.Log
import com.example.proof_of_concept.BuildConfig
import com.google.maps.android.PolyUtil
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.internal.wait
import org.json.JSONObject
import org.osmdroid.util.GeoPoint

suspend fun getRouteFromOpenRouteService(routeOption: String, locationStart: GeoPoint, locationEnd: GeoPoint): List<List<GeoPoint>> {
    val OpenRouteService = "https://api.openrouteservice.org/v2/directions/${routeOption}"
    val apiKey = BuildConfig.API_KEY
    val jsonBody = JSONObject()

    val coordinates = listOf(
        listOf(locationStart.longitude, locationStart.latitude),
        listOf(locationEnd.longitude, locationEnd.latitude)
    )
    jsonBody.put("coordinates", coordinates)

    val alternativeRoutes = JSONObject()
    alternativeRoutes.put("target_count", 3)
    jsonBody.put("alternative_routes", alternativeRoutes)

    val locations = mutableListOf<List<GeoPoint>>()

    try {
        val jsonObject = sendRequestWithAuthorizationAndBody(OpenRouteService, apiKey, jsonBody.toString(), "application/json".toMediaType())

        val routes = jsonObject.getJSONArray("routes")
        for (i in 0 until routes.length()) {
            val geometryLineStep = routes.getJSONObject(i).getString("geometry")

            val decodeStep = PolyUtil.decode(geometryLineStep)
            val route = mutableListOf<GeoPoint>()
            decodeStep.forEach { route.add(GeoPoint(it.latitude, it.longitude)) }

            locations.add(route)
        }
    } catch (e: Exception) {
        Log.e("getRouteFromOpenRouteService", "Failed to get route: ${e.message}")
    }

    return locations
}
