package com.example.proof_of_concept

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.Manifest
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

class MainActivity : ComponentActivity() {

    private val mainScope = CoroutineScope(Dispatchers.Main)
    private var map: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().userAgentValue = packageName

        setContent {
            val context = LocalContext.current
            var hasPermission by remember { mutableStateOf(hasLocationPermission(context)) }
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    hasPermission = isGranted
                }
            )

            executeAsync {
                if (!hasPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
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


                    // MAP need to be not null LATER
                    Main_App(map, LocalContext.current)
                }
            }
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