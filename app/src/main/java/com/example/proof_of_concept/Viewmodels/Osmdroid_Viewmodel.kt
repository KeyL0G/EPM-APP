package com.example.proof_of_concept.Viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proof_of_concept.Api_Helper.getMarkerOnLocation
import com.example.proof_of_concept.Api_Helper.getRouteFromOpenRouteService
import com.example.proof_of_concept.Api_Helper.getRoutesFromOSRM
import com.example.proof_of_concept.Api_Helper.getStreet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

data class ToiletDetail(val fullAddress: String = "", val location: GeoPoint = GeoPoint(0.0,0.0), val options: List<String> = mutableListOf())
data class Routes(
    val routeCar: List<List<GeoPoint>>,
    val routeFoot: List<List<GeoPoint>>,
    val routeBike: List<List<GeoPoint>>,
    val routeAccess: List<List<GeoPoint>>,
)

class Osmdroid_Viewmodel: ViewModel() {
    private val _currentToilets: MutableLiveData<List<ToiletDetail>> = MutableLiveData(emptyList())
    val currentToilets: LiveData<List<ToiletDetail>> = _currentToilets

    private val _currentSelectedToilet: MutableLiveData<ToiletDetail> = MutableLiveData()
    val currentSelectedToilet: LiveData<ToiletDetail> = _currentSelectedToilet

    private val _routes: MutableLiveData<Routes> = MutableLiveData()
    val routes: LiveData<Routes> = _routes

    fun setRoutes(routes: Routes) {
        _routes.value = routes
    }

    fun setCurrentSelectedToilet(toilet: ToiletDetail){
        _currentSelectedToilet.value = toilet
    }

    fun setToilets(toilet: ToiletDetail) {
        val newToiletList = _currentToilets.value!!.toMutableList()
        newToiletList.add(toilet)
        _currentToilets.value = newToiletList
    }

    fun getToiletsFromLocation(mapView: MapView, context: Context, location: GeoPoint) {
        viewModelScope.launch {
            try {
                val toilets = getMarkerOnLocation(mapView, context, location)
                toilets.forEach { toilet ->
                    val street = getStreet(toilet.geoPoint)
                    setToilets(ToiletDetail(street, toilet.geoPoint, toilet.option))
                    delay(1000L)
                }
            } catch (e: Exception) {
                Log.e("Osmdroid_Viewmodel", "Failed to get toilets from location: ${e.message}")
            }
        }
    }

    fun getRoutes(currentLocation: GeoPoint, toLocation: GeoPoint) {
        viewModelScope.launch {
            try {
                val routeCar = getSpecialRoute("routed-car", "driving-car", currentLocation, toLocation)
                val routeFoot = getSpecialRoute("routed-foot", "foot-walking", currentLocation, toLocation)
                val routeBike = getSpecialRoute("routed-bike", "cycling-regular", currentLocation, toLocation)
                val routeAccess = getSpecialRoute("routed-foot", "wheelchair", currentLocation, toLocation).asReversed()
                val allRoutes = Routes(routeCar, routeFoot, routeBike, routeAccess)
                setRoutes(allRoutes)
            } catch (e: Exception) {
                Log.e("Osmdroid_Viewmodel", "Failed to get routes: ${e.message}")
            }
        }
    }

    private suspend fun getSpecialRoute(optionA: String, optionB: String, currentLocation: GeoPoint, toLocation: GeoPoint): List<List<GeoPoint>> {
        val getRouteFromOSRM = getRoutesFromOSRM(optionA, currentLocation, toLocation)
        val getRouteFromOpenRouteService = getRouteFromOpenRouteService(optionB, currentLocation, toLocation)
        val allRoute = mutableListOf<List<GeoPoint>>()
        allRoute.add(getRouteFromOSRM)
        getRouteFromOpenRouteService.forEach { allRoute.add(it) }
        return allRoute
    }
}
