package com.example.proof_of_concept

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.proof_of_concept.NotificationHelper.hasLocationPermission
import com.example.proof_of_concept.ui.theme.Proof_Of_ConceptTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private val mainScope = CoroutineScope(Dispatchers.Main)
    private var map: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set user agent for osmdroid
        Configuration.getInstance().userAgentValue = packageName

        setContent {
            val context = LocalContext.current
            var hasPermission by remember { mutableStateOf(hasLocationPermission(context)) }
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    hasPermission = isGranted
                    if (isGranted) {
                        // Update map to current location if permission granted
                        updateMapToCurrentLocation(context)
                    }
                }
            )

            // Request permission if not granted
            executeAsync {
                if (!hasPermission) {
                    when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                            permissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        }
                        else -> {
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                    }
                }
            }

            Proof_Of_ConceptTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    AndroidView(
                        factory = { context ->
                            MapView(context).apply {
                                map = this
                                map?.setTileSource(TileSourceFactory.MAPNIK)
                                map?.isTilesScaledToDpi = true
                                map?.setMultiTouchControls(true)
                                map?.controller?.setZoom(10.0)
                                map?.controller?.setCenter(GeoPoint(50.93450168288072, 7.0268959013448296))
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    // MAP should not be null before use
                    Main_App(map, LocalContext.current) {
                        executeAsync {
                            if (!hasPermission) {
                                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateMapToCurrentLocation(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                // Ensure location services are enabled
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    Log.e("Location", "Location services are disabled.")
                    return
                }

                // Request location updates if location services are available
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000L,  // Interval between updates (ms)
                    1f,  // Minimum distance between updates (meters)
                    { updatedLocation ->
                        val geoPoint = GeoPoint(updatedLocation.latitude, updatedLocation.longitude)
                        map?.controller?.setCenter(geoPoint)
                        map?.controller?.setZoom(15.0)  // Set zoom level
                        Log.d("LocationUpdate", "Location updated to: ${updatedLocation.latitude}, ${updatedLocation.longitude}")
                    }
                )

                // Request updates from NETWORK_PROVIDER as a fallback if GPS is unavailable
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000L,  // Interval between updates (ms)
                    1f,  // Minimum distance between updates (meters)
                    { updatedLocation ->
                        val geoPoint = GeoPoint(updatedLocation.latitude, updatedLocation.longitude)
                        map?.controller?.setCenter(geoPoint)
                        map?.controller?.setZoom(15.0)
                        Log.d("LocationUpdate", "Location updated to: ${updatedLocation.latitude}, ${updatedLocation.longitude}")
                    }
                )

            } catch (e: SecurityException) {
                Log.e("Location", "SecurityException: ${e.message}")
            } catch (e: Exception) {
                Log.e("Location", "Unexpected error: ${e.message}")
            }
        } else {
            Log.e("Permission", "Location permission not granted.")
        }
    }


    override fun onResume() {
        super.onResume()
        map?.onResume()
    }

    override fun onPause() {
        super.onPause()
        map?.onPause()
    }

    private fun executeAsync(asyncCallBack: () -> Unit) {
        mainScope.launch {
            try {
                asyncCallBack()
            } catch (error: Exception) {
                Log.e("Error-Setting", "$error")
            }
        }
    }
}
