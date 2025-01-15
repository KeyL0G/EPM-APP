package com.example.proof_of_concept.Viewmodels

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proof_of_concept.Helper.getCurrentLocation
import com.example.proof_of_concept.R
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

class Map_Viewmodel: ViewModel() {
    private val _currentLocation: MutableLiveData<GeoPoint> = MutableLiveData()
    val currentLocation: LiveData<GeoPoint> = _currentLocation
    var oldLocation: GeoPoint = currentLocation.value?: GeoPoint(0.0,0.0)
    private val _map: MutableLiveData<MapView> = MutableLiveData()
    val map: LiveData<MapView> = _map
    private val _currentRoute: MutableLiveData<List<GeoPoint>> = MutableLiveData()
    val currentRoute: LiveData<List<GeoPoint>> = _currentRoute
    var oldRoute: List<GeoPoint> = currentRoute.value?: mutableListOf(GeoPoint(0.0,0.0))

    fun updateRoute(route: List<GeoPoint>) {

        if (currentRoute.value != null){
            oldRoute = currentRoute.value!!
        }
        _currentRoute.value = route

    }

    fun updateLocation(context: Context) {
        getCurrentLocation(context) { location ->
         if (currentLocation.value != null){
             oldLocation = currentLocation.value!!
         }
            _currentLocation.value = location
        }
    }

    fun moveMapToCurrentLocation(context: Context) {
        if (map.value != null && currentLocation.value != null) {
            val existingMarker = map.value!!.overlays.filterIsInstance<Marker>().firstOrNull {
                it.position.latitude == oldLocation.latitude && it.position.longitude == oldLocation.longitude
            }
            if (existingMarker != null) {
                map.value!!.overlays.remove(existingMarker)
            }
            val marker = Marker(map.value!!)
            marker.position = currentLocation.value!!
            marker.icon = ContextCompat.getDrawable(context, R.drawable.near_me_blue)!!
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            map.value!!.overlays.add(marker)
            map.value!!.controller.setZoom(15.0)
            map.value!!.controller.setCenter(currentLocation.value!!)
        } else {
            Log.e("MAP_VIEWMOEL", "currentLocation or map are null")
        }
    }

    fun drawRoute(route: List<GeoPoint>) {
        var removeLine = Polyline()
        removeLine.setPoints(oldRoute)
        removeLine?.let {
            var overlayFilter = map.value?.overlays?.filter { it is Polyline }
            overlayFilter?.forEach{
                if (it is Polyline){
                    map.value?.overlays?.remove(it)
                }
            }
        }


        val line = Polyline()
        line.setPoints(route)
        map.value?.overlays?.add(line)
        map.value?.invalidate()
    }


    fun updateMap(newMap: MapView) {
        _map.value = newMap
    }

    fun onResume() = _map.value?.onResume()
    fun onPause() = _map.value?.onPause()

}