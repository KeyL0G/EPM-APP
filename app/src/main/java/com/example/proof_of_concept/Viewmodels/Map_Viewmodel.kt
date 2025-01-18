package com.example.proof_of_concept.Viewmodels

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proof_of_concept.CompassSensor
import com.example.proof_of_concept.Helper.getCurrentLocation
import com.example.proof_of_concept.R
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

enum class Navigation_Page {
    SETTINGS,
    START,
    LOCATIONDESCRIPTION,
    LOCATIONNAVIGATION,
    STARTROUTE
}

class Map_Viewmodel: ViewModel() {
    private val _currentLocation: MutableLiveData<GeoPoint> = MutableLiveData()
    val currentLocation: LiveData<GeoPoint> = _currentLocation
    var oldLocation: GeoPoint = currentLocation.value?: GeoPoint(0.0,0.0)
    private val _map: MutableLiveData<MapView> = MutableLiveData()
    val map: LiveData<MapView> = _map
    private val _currentRoute: MutableLiveData<List<GeoPoint>> = MutableLiveData()
    val currentRoute: LiveData<List<GeoPoint>> = _currentRoute
    var oldRoute: List<GeoPoint> = currentRoute.value?: mutableListOf(GeoPoint(0.0,0.0))
    private val _hasPermission: MutableLiveData<Boolean> = MutableLiveData(false)
    val hasPermission: LiveData<Boolean> = _hasPermission
    private val _navigationPage: MutableLiveData<Navigation_Page> = MutableLiveData()
    val navigationPage: LiveData<Navigation_Page> = _navigationPage

    private var marker: Marker? = null

    fun updateNavigationPage(value: Navigation_Page){
        _navigationPage.value = value
    }

    fun updatePermission(value: Boolean){
        _hasPermission.value = value
    }

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

    private var compassSensor: CompassSensor? = null

    fun startCompassTracking(context: Context) {
        compassSensor = CompassSensor(context) { azimuth ->
            val correctedAzimuth = (360 - azimuth) % 360
            marker?.rotation = correctedAzimuth+45
            map.value?.invalidate()
        }
        compassSensor?.start()
    }


    fun stopCompassTracking() {
        compassSensor?.stop()
        compassSensor = null
    }

    override fun onCleared() {
        super.onCleared()
        stopCompassTracking()
    }


    fun moveMapToCurrentLocation(context: Context) {
        val mapView = map.value
        val newLocation = currentLocation.value

        if (mapView != null && newLocation != null) {
            // Entferne alten Marker, falls vorhanden
            marker?.let {
                mapView.overlays.remove(it)
            }

            // Erstelle einen neuen Marker und speichere ihn in der Instanzvariable
            marker = Marker(mapView).apply {
                position = newLocation
                icon = ContextCompat.getDrawable(context, R.drawable.near_me_blue)!!
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            }

            // Füge den neuen Marker hinzu
            mapView.overlays.add(marker)
            mapView.controller.setZoom(18.0)
            mapView.controller.setCenter(newLocation)

            // Starte den Kompass-Tracker
            startCompassTracking(context)
        } else {
            Log.e("MAP_VIEWMODEL", "currentLocation or map are null")
        }
    }




    fun deleteRouteFromMap(route: List<GeoPoint>?){
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