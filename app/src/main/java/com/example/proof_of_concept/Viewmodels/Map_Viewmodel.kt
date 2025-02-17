package com.example.proof_of_concept.Viewmodels

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proof_of_concept.Api_Helper.Steps
import com.example.proof_of_concept.CompassSensor
import com.example.proof_of_concept.Helper.getCurrentLocation
import com.example.proof_of_concept.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

enum class Navigation_Page {
    SETTINGS,
    START,
    LOCATIONDESCRIPTION,
    LOCATIONNAVIGATION,
    STARTROUTE,
    BEWERTUNGEN
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
    private val _currentSteps: MutableLiveData<List<Steps>> = MutableLiveData()
    val currentSteps = _currentSteps

    private var marker: Marker? = null
    private var compassSensor: CompassSensor? = null

    fun updateCurrentSteps(value: List<Steps>) {
        _currentSteps.value = value
    }

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

            // Erstelle einen neuen Marker, falls er noch nicht existiert
            if (marker == null) {
                marker = Marker(mapView).apply {
                    icon = ContextCompat.getDrawable(context, R.drawable.near_me_blue)!!
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                }
            }

            // Füge den Marker zur Karte hinzu (falls nicht bereits geschehen)
            if (!mapView.overlays.contains(marker)) {
                mapView.overlays.add(marker)
            }

            // Animation zwischen aktueller und neuer Marker-Position
            val startLatMarker = marker?.position?.latitude ?: newLocation.latitude
            val startLonMarker = marker?.position?.longitude ?: newLocation.longitude
            val startPointMarker = GeoPoint(startLatMarker, startLonMarker)

            // Animation zwischen aktueller und neuer Kamera-Position
            val startLatCamera = mapView.mapCenter.latitude
            val startLonCamera = mapView.mapCenter.longitude
            val startPointCamera = GeoPoint(startLatCamera, startLonCamera)

            val animationSteps = 20
            val stepDuration = 50L // Millisekunden
            val deltaLatMarker = (newLocation.latitude - startLatMarker) / animationSteps
            val deltaLonMarker = (newLocation.longitude - startLonMarker) / animationSteps
            val deltaLatCamera = (newLocation.latitude - startLatCamera) / animationSteps
            val deltaLonCamera = (newLocation.longitude - startLonCamera) / animationSteps

            // Animationslogik mit Coroutine
            viewModelScope.launch {
                for (i in 1..animationSteps) {
                    // Aktualisiere die Marker-Position
                    marker?.position = GeoPoint(
                        startPointMarker.latitude + deltaLatMarker * i,
                        startPointMarker.longitude + deltaLonMarker * i
                    )

                    // Aktualisiere die Kamera-Position
                    val animatedCameraPosition = GeoPoint(
                        startPointCamera.latitude + deltaLatCamera * i,
                        startPointCamera.longitude + deltaLonCamera * i
                    )
                    mapView.controller.setCenter(animatedCameraPosition)

                    mapView.postInvalidate() // Karte aktualisieren
                    delay(stepDuration)
                }

                // Setze die endgültige Position und zentriere die Karte
                marker?.position = newLocation
                mapView.controller.setZoom(18.0)
                mapView.controller.setCenter(newLocation)
            }

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