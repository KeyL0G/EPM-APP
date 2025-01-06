package com.example.proof_of_concept
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.proof_of_concept.ui.theme.Proof_Of_ConceptTheme
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class MainActivity : ComponentActivity() {

    private var map: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().userAgentValue = packageName

        setContent {
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

                    if(map != null) {
                        Main_App(map!!, LocalContext.current)
                    }
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
}