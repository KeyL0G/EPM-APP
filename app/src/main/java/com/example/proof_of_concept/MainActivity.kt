package com.example.proof_of_concept

import android.Manifest
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
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
    private val mapViewModel: Map_Viewmodel by viewModels()
    private val osmdroidViewModel: Osmdroid_Viewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().userAgentValue = packageName

        setContent {
            val map by mapViewModel.map.observeAsState(initial = null)
            val location by mapViewModel.currentLocation.observeAsState(initial = null)
            val context = LocalContext.current
            var hasPermission by remember { mutableStateOf(hasLocationPermission(context)) }

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    hasPermission = isGranted
                    mapViewModel.updatePermission(hasPermission)

                    if (isGranted) {
                        mapViewModel.updateLocation(context)
                    } else {
                        Log.e("Permission", "Location permission denied")
                    }
                }
            )

            Proof_Of_ConceptTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    AndroidView(
                        factory = { context ->
                            MapView(context).apply {
                                setTileSource(TileSourceFactory.MAPNIK)
                                isTilesScaledToDpi = true
                                setMultiTouchControls(true)
                                mapViewModel.updateMap(this)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    Main_App(context = context)
                }

                LaunchedEffect(key1 = hasPermission) {

                    if (!hasPermission) {
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }else{
                        mapViewModel.updateLocation(context)
                    }
                }

                LaunchedEffect(key1 = map, key2 = location) {
                    if (map != null && location != null) {
                        mapViewModel.moveMapToCurrentLocation(context)
                        osmdroidViewModel.getToiletsFromLocation(map!!, context, location!!)
                    } else {
                        Log.e("WARN", "Map or Location is not ready")
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapViewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapViewModel.onPause()
    }
}
