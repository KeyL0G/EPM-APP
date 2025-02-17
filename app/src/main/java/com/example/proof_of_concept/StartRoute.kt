package com.example.proof_of_concept

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun StartRoute(onNavigationClick: () -> Unit) {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    val map_viewmodel: Map_Viewmodel = viewModel()
    val currentLocation by map_viewmodel.currentLocation.observeAsState(initial = null)
    val currentRoute by map_viewmodel.currentRoute.observeAsState(initial = null)
    val currentSteps by map_viewmodel.currentSteps.observeAsState(initial = null)
    var lastSpokenStepIndex by remember { mutableStateOf(-1) }
    var isTtsReady by remember { mutableStateOf(false) }
    var isSpeaking by remember { mutableStateOf(false) }

    // Variable to store last spoken voiceline
    var lastSpokenVoiceline by remember { mutableStateOf("") }

    // Initialize TextToSpeech
    LaunchedEffect(currentSteps) {
        if (currentSteps != null && tts == null) {
            tts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.GERMAN
                    isTtsReady = true
                    tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {
                            isSpeaking = true
                        }

                        override fun onDone(utteranceId: String?) {
                            isSpeaking = false
                        }

                        override fun onError(utteranceId: String?) {
                            isSpeaking = false
                        }
                    })
                }
            }
        }
    }

    var spokenInstructions = remember { mutableSetOf<String>() }

    LaunchedEffect(currentLocation) {
        if (isTtsReady && !isSpeaking) {
            currentSteps?.let { steps ->
                currentLocation?.let { location ->
                    currentRoute?.let { route ->
                        steps.forEachIndexed { index, element ->
                            val step = getCurrentStep(location, element, route)
                            if (step != null && index != lastSpokenStepIndex && !spokenInstructions.contains(step.instructions)) {
                                Log.e("step:", "true")
                                tts?.speak(step.instructions, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
                                lastSpokenStepIndex = index
                                lastSpokenVoiceline = step.instructions // Store the last spoken voiceline
                                spokenInstructions.add(step.instructions)
                                isSpeaking = true
                            }
                        }
                    }
                }
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
                // Repeat the last spoken voiceline if available
                if (lastSpokenVoiceline.isNotEmpty()) {
                    tts?.speak(lastSpokenVoiceline, TextToSpeech.QUEUE_FLUSH, null, "repeatUtterance")
                }
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

    // Clean up TextToSpeech resources
    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }
}

fun getCurrentStep(currentLocation: GeoPoint, currentSteps: Steps, currentRoute: List<GeoPoint>): Steps? {
    val thresholdDistance = 50.0 // Define a threshold distance in meters

    for (index in currentRoute.indices) {
        val stepLocation = currentRoute[index]
        val distance = currentLocation.distanceTo(stepLocation)

        if (distance <= thresholdDistance) {
            if (currentSteps.waypoints[0] <= index && currentSteps.waypoints[1] >= index) {
                return currentSteps
            }
        }
    }
    return null
}

fun GeoPoint.distanceTo(other: GeoPoint): Double {
    val earthRadius = 6371000.0 // Radius of the earth in meters
    val dLat = Math.toRadians(other.latitude - this.latitude)
    val dLon = Math.toRadians(other.longitude - this.longitude)
    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(this.latitude)) * cos(Math.toRadians(other.latitude)) *
            sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return earthRadius * c
}
