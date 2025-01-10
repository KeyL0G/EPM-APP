package com.example.proof_of_concept.Helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import org.osmdroid.util.GeoPoint

fun hasLocationPermission(context: Context): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        return true
    }
}

fun getCurrentLocation(context: Context, setNewLocation: (geoPoint: GeoPoint) -> Unit) {
    try {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.e("Location", "Location services are disabled.")
            return
        }

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 1f)
            { updatedLocation ->
                 setNewLocation(GeoPoint(updatedLocation.latitude, updatedLocation.longitude))
            }
        }
    } catch (e: Exception) {
        Log.e("Location", "Unexpected error: ${e.message}")
    }
}