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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.proof_of_concept.Helper.hasLocationPermission
import com.example.proof_of_concept.ui.theme.Proof_Of_ConceptTheme
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import com.example.proof_of_concept.Viewmodels.Map_Viewmodel
import com.example.proof_of_concept.Viewmodels.Osmdroid_Viewmodel

class MainActivity : ComponentActivity() {
    private val map_viewmodel: Map_Viewmodel by viewModels()
    private val osmdroid_viewmodel: Osmdroid_Viewmodel by viewModels()

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
            val map by map_viewmodel.map.observeAsState(initial = null)
            val location by map_viewmodel.currentLocation.observeAsState(initial = null)

            Proof_Of_ConceptTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    AndroidView(
                        factory = { context ->
                            MapView(context).apply {
                                map_viewmodel.updateMap(this)
                                map?.setTileSource(TileSourceFactory.MAPNIK)
                                map?.isTilesScaledToDpi = true
                                map?.setMultiTouchControls(true)

                                if (hasPermission) {
                                    map_viewmodel.updateLocation(context)
                                    map_viewmodel.moveMapToCurrentLocation()
                                    if(map != null && location != null)
                                        osmdroid_viewmodel.getToiletsFromLocation(map!!, context, location!!)
                                    else
                                        Log.e("WARN", "Location or Map not found")
                                } else {
                                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                    map_viewmodel.updateLocation(context)
                                    map_viewmodel.moveMapToCurrentLocation()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    Main_App(context = context)
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
}
