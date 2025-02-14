package com.example.proof_of_concept.Api_Helper

import android.util.Log
import com.google.maps.android.PolyUtil
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONArray
import org.json.JSONObject
import org.osmdroid.util.GeoPoint

data class Route(val distance : Double, val duration: Double, val instructions: List<String>,val geometry: List<GeoPoint>)
data class AllRoutes(val route: MutableList<Route> = mutableListOf())

suspend fun getRouteFromOpenRouteService(routeOption: String, locationStart: GeoPoint, locationEnd: GeoPoint): AllRoutes {
    val OpenRouteService = "https://api.openrouteservice.org/v2/directions/${routeOption}"
    val apiKey = com.example.proof_of_concept.BuildConfig.API_KEY
    val jsonBody = JSONObject()

    val coordinatesArray = JSONArray()

    val startCoordinate = JSONArray().apply {
        put(locationStart.longitude)
        put(locationStart.latitude)
    }

    val endCoordinate = JSONArray().apply {
        put(locationEnd.longitude)
        put(locationEnd.latitude)
    }

    coordinatesArray.put(startCoordinate)
    coordinatesArray.put(endCoordinate)

    jsonBody.put("coordinates", coordinatesArray)

    val alternativeRoutes = JSONObject()
    alternativeRoutes.put("target_count", 3)
    jsonBody.put("alternative_routes", alternativeRoutes)

    val allRoutes = AllRoutes()

    try {
        val jsonObject = sendRequestWithAuthorizationAndBody(OpenRouteService, apiKey, jsonBody.toString(), "application/json".toMediaType())

        val routes = jsonObject.getJSONArray("routes")
        for (i in 0 until routes.length()) {
            val summary = routes.getJSONObject(i).getJSONObject("summary")
            val distance = summary.getDouble("distance")
            val duration = summary.getDouble("duration")
            val segments = routes.getJSONObject(i).getJSONArray("segments")
            val stepsList = mutableListOf<String>()
            for (j in 0 until segments.length()) {
                val segmentElement = segments.getJSONObject(i)
                val steps = segmentElement.getJSONArray("steps")
                for (l in 0 until segments.length()) {
                    stepsList.add(steps.getJSONObject(j).getString("instruction"))
                }

            }

                val geometryLineStep = routes.getJSONObject(i).getString("geometry")

            val decodeStep = PolyUtil.decode(geometryLineStep)
            val route = mutableListOf<GeoPoint>()
            decodeStep.forEach { route.add(GeoPoint(it.latitude, it.longitude)) }
            allRoutes.route.add(Route(distance,duration,stepsList,route))

        }
    } catch (e: Exception) {
        Log.e("getRouteFromOpenRouteService", "Failed to get route: ${e.message}")
    }

    return allRoutes
}

