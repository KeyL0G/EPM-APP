package com.example.proof_of_concept.Viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proof_of_concept.Helper.getCurrentLocation
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline

class Map_Viewmodel: ViewModel() {
    private val _currentLocation: MutableLiveData<GeoPoint> = MutableLiveData()
    val currentLocation: LiveData<GeoPoint> = _currentLocation

    private val _map: MutableLiveData<MapView> = MutableLiveData()
    val map: LiveData<MapView> = _map

    fun updateLocation(context: Context) {
        getCurrentLocation(context) { location ->
            _currentLocation.value = location
        }
    }

    fun moveMapToCurrentLocation() {
        if (map.value != null && currentLocation.value != null) {
            map.value!!.controller.setZoom(20.0)
            map.value!!.controller.setCenter(currentLocation.value!!)
        } else {
            Log.e("MAP_VIEWMOEL", "currentLocation or map are null")
        }
    }

    fun drawRoute(route: List<GeoPoint>) {
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