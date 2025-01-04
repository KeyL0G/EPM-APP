package com.example.proof_of_concept.Viewmodels

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.maps.android.PolyUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import okio.IOException
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline


class OSM_Viewmodel: ViewModel() {

    private val OverPassURL = "https://overpass-api.de/api/interpreter"
    private val Route_OSRM = "https://routing.openstreetmap.de/routed-foot/route/v1/foot/7.0268959013448296,50.93450168288072;7.00553677156708,50.9615938331697?overview=full&steps=true"

    fun addMarkerOnMap(mapView: MapView, context: Context){
        val query = """
            [out:json];
            area[name="Köln"]->.searchArea;
            node["amenity"="toilets"](area.searchArea);
            out body;
        """.trimIndent()



        sendRequestWithBody(OverPassURL, query, "text/plain; charset=utf-8".toMediaType()) { jsonObject ->
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
                marker.icon = ContextCompat.getDrawable(context, org.osmdroid.library.R.drawable.marker_default_focused_base)!!
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                mapView.overlays.add(marker)
            }

            mapView.invalidate()
        }
    }

    fun addRouteOnMap(mapView: MapView) {
        sendRequest(Route_OSRM){ jsonObject ->
            val locations = mutableListOf<GeoPoint>()
            val line = Polyline()

            val routes = jsonObject.getJSONArray("routes")
            val geometryLineStep = routes.getJSONObject(0).getString("geometry")
            val decodeStep = PolyUtil.decode(geometryLineStep)
            decodeStep.forEach{
                Log.i("GEOMETRY","${it.latitude},${it.longitude}")
                locations.add(GeoPoint(it.latitude, it.longitude))
            }

            line.setPoints(locations)
            mapView.overlays.add(line)
        }

        mapView.invalidate()
    }

    private fun sendRequest(URL: String, callback: (jsonObject: JSONObject) -> Unit) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(URL)
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("RequestHandler", "Message: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseAsJson = JSONObject(response.body!!.string())
                callback(responseAsJson)
            }
        })

    }

    private fun sendRequestWithBody(URL: String, query: String, mediaType: MediaType, callback: (jsonObject: JSONObject) -> Unit) {
        val client = OkHttpClient()
        val body = query.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(URL)
            .post(body)
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("RequestHandler", "Message: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseAsJson = JSONObject(response.body!!.string())
                callback(responseAsJson)
            }
        })
    }



}