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
        val getRouteFromOSRM = getRoutesFromOSRM("", GeoPoint(0.0,0.0), GeoPoint(0.0,0.0))
        val getRouteFromOpenRouteService = getRouteFromOpenRouteService("", GeoPoint(0.0,0.0), GeoPoint(0.0,0.0))
        val allRoute = mutableListOf<List<GeoPoint>>()
        allRoute.add(getRouteFromOSRM)
        getRouteFromOpenRouteService.forEach{ allRoute.add(it) }

        val line = Polyline()
        line.setPoints(allRoute[0]) // Aktuell wird nur die erste route direkt genommen
        mapView.overlays.add(line)
    }
}