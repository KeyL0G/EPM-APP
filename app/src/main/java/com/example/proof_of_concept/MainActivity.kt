package com.example.proof_of_concept

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.proof_of_concept.ui.theme.Proof_Of_ConceptTheme
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView

class MainActivity : ComponentActivity() {

    private var map: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().userAgentValue = packageName

        setContent {
            Proof_Of_ConceptTheme {
                startScreen { mapView ->
                    map = mapView
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
