package com.example.proof_of_concept

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proof_of_concept.Api_Helper.Steps
import com.example.proof_of_concept.Viewmodels.Map_Viewmodel
import org.osmdroid.util.GeoPoint
import java.util.Locale

@Composable
fun StartRoute(onNavigationClick: () -> Unit) {
    val context = LocalContext.current
    var tts by remember  { mutableStateOf<TextToSpeech?>(null)}
    val map_viewmodel: Map_Viewmodel = viewModel()
    val currentLocation by map_viewmodel.currentLocation.observeAsState(initial = null)
    val currentRoute by map_viewmodel.currentRoute.observeAsState(initial = null)
    val currentSteps by map_viewmodel.currentSteps.observeAsState(initial = null)

    LaunchedEffect(Unit) {
        if (currentSteps != null) {
            tts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.GERMAN
                }
            }
        }
    }

    LaunchedEffect(currentLocation) {
        currentSteps!!.forEach{ element ->
            val step = getCurrentStep(currentLocation!!,element,currentRoute!!)
            if (step != null) {
                Log.e("step:","true")
                tts?.speak(step.instructions,TextToSpeech.QUEUE_FLUSH,null,null)
            }
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

                Image(
                    painter = painterResource(id = R.drawable.arrow_upward_black),
                    contentDescription = "Richtung"
                )
            Text(text = "Beispiel - Straße", fontWeight = FontWeight.Bold)
            IconButton(onClick = {
                tts?.speak("Yousef du Bastard",TextToSpeech.QUEUE_FLUSH,null,null)
            }) {
                Icon(Icons.Default.Refresh, contentDescription = "Aktualisieren")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(8.dp)


        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Column(
                    Modifier
                        .background(
                            Color.White.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(9.dp)
                        )
                        .padding(4.dp)
                ) {

                    Text(text = "10 min", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = "500 m", fontSize = 14.sp)
                    Text(text = "Ankunft: 17:10", fontSize = 14.sp)
                }
                Button(
                    onClick = onNavigationClick,
                    colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Text(text = "Zurück", color = Color.White)
                }
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }
}
 fun getCurrentStep(currentLocation: GeoPoint, currentSteps: Steps, currentRoute:List<GeoPoint>): Steps? {
    for (index in currentRoute.indices) {
        if (currentRoute[index] == currentLocation) {
            if (currentSteps.waypoints[0] <= index && currentSteps.waypoints[1] >= index) {
                return currentSteps
            }
        }
    }
     return null
 }