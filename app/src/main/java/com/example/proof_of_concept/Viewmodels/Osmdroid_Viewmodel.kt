package com.example.proof_of_concept.Viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.proof_of_concept.Api_Helper.getMarkerOnLocation
import com.example.proof_of_concept.Api_Helper.getRouteFromOpenRouteService
import com.example.proof_of_concept.Api_Helper.getRoutesFromOSRM
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline


class Osmdroid_Viewmodel: ViewModel() {

    fun getToiletsFromLocation(mapView: MapView, context: Context, location: GeoPoint){
        getMarkerOnLocation(mapView, context, location)
    }

    fun getRoutes(mapView: MapView){
        val getRouteFromOSRM = getRoutesFromOSRM("routed-foot", GeoPoint(50.93450168288072,7.0268959013448296), GeoPoint(50.9615938331697,7.00553677156708))
        val getRouteFromOpenRouteService = getRouteFromOpenRouteService("foot-walking", GeoPoint(50.93450168288072,7.0268959013448296), GeoPoint(50.9615938331697,7.00553677156708))
        val allRoute = mutableListOf<List<GeoPoint>>()
        allRoute.add(getRouteFromOSRM)
        getRouteFromOpenRouteService.forEach{ allRoute.add(it) }

        val line = Polyline()
        line.setPoints(allRoute[0]) // Aktuell wird nur die erste route direkt genommen
        mapView.overlays.add(line)
    }
}