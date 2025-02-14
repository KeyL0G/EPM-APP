package com.example.proof_of_concept.Viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proof_of_concept.Api_Helper.AllRoutes
import com.example.proof_of_concept.Api_Helper.getMarkerOnLocation
import com.example.proof_of_concept.Api_Helper.getRouteFromOpenRouteService
import com.example.proof_of_concept.Api_Helper.getStreet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

data class ToiletDetail(val fullAddress: String = "", val location: GeoPoint = GeoPoint(0.0,0.0), val options: List<String> = mutableListOf())
data class AllRoutesFromAPI(
    val Car: AllRoutes,
    val Foot: AllRoutes,
    val Bike: AllRoutes,
    val Access: AllRoutes,
)

class Osmdroid_Viewmodel: ViewModel() {
    private val _currentToilets: MutableLiveData<List<ToiletDetail>> = MutableLiveData(emptyList())
    val currentToilets: LiveData<List<ToiletDetail>> = _currentToilets

    private val _currentSelectedToilet: MutableLiveData<ToiletDetail> = MutableLiveData()
    val currentSelectedToilet: LiveData<ToiletDetail> = _currentSelectedToilet

    private val _allRoutesFromAPI: MutableLiveData<AllRoutesFromAPI> = MutableLiveData()
    val allRoutesFromAPI: LiveData<AllRoutesFromAPI> = _allRoutesFromAPI

    fun setAllRoutesFromAPI(routes: AllRoutesFromAPI) {
        _allRoutesFromAPI.value = routes
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
                val routeCar = getSpecialRoute( "driving-car", currentLocation, toLocation)
                val routeFoot = getSpecialRoute( "foot-walking", currentLocation, toLocation)
                val routeBike = getSpecialRoute( "cycling-regular", currentLocation, toLocation)
                val routeAccess = getSpecialRoute( "wheelchair", currentLocation, toLocation)
                val allRoutes = AllRoutesFromAPI(routeCar, routeFoot, routeBike, routeAccess)
                setAllRoutesFromAPI(allRoutes)
            } catch (e: Exception) {
                Log.e("Osmdroid_Viewmodel", "Failed to get routes: ${e.message}")
            }
        }
    }

    private suspend fun getSpecialRoute(option: String, currentLocation: GeoPoint, toLocation: GeoPoint): AllRoutes = getRouteFromOpenRouteService(option, currentLocation, toLocation)
}
