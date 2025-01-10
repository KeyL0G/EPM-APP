package com.example.proof_of_concept

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.proof_of_concept.Helper.hasLocationPermission
import com.example.proof_of_concept.ui.theme.Proof_Of_ConceptTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import com.example.proof_of_concept.Viewmodels.Map_Viewmodel

class MainActivity : ComponentActivity() {

    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val map_viewmodel: Map_Viewmodel by viewModels()
    private var map: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().userAgentValue = packageName

        setContent {
            val context = LocalContext.current
            var hasPermission by remember { mutableStateOf(hasLocationPermission(context)) }
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted -> hasPermission = isGranted }
            )

            val currentLocation: GeoPoint =
                if(map_viewmodel.currentLocation.value != null)
                    map_viewmodel.currentLocation.value!!
                else
                    GeoPoint(0.0,0.0)

            Proof_Of_ConceptTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    AndroidView(
                        factory = { context ->
                            MapView(context).apply {
                                map_viewmodel.updateMap(this)
                                map?.setTileSource(TileSourceFactory.MAPNIK)
                                map?.isTilesScaledToDpi = true
                                map?.setMultiTouchControls(true)
                                map?.controller?.setZoom(15.0)
                                map?.controller?.setCenter(currentLocation)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    Main_App()
                }
            }

            executeAsync {
                if (!hasPermission) {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    map_viewmodel.updateLocation(context)
                    map_viewmodel.moveMapToCurrentLocation()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        map_viewmodel.onResume()
    }

    override fun onPause() {
        super.onPause()
        map_viewmodel.onPause()
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
