package com.example.proof_of_concept

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
fun startScreen(onSettingsClick: () -> Unit) {
    var showLocationDialog by remember { mutableStateOf(false) }
    var buttonText by remember { mutableStateOf("In der Nähe suchen?") }

    Box(modifier = Modifier.fillMaxSize()) {
        // Karte
        AndroidView(
            factory = { context ->
                MapView(context).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    isTilesScaledToDpi = true
                    setMultiTouchControls(true)
                    controller.setZoom(10.0)
                    controller.setCenter(GeoPoint(50.93450168288072, 7.0268959013448296))
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Suchfeld und Button oben
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { showLocationDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(buttonText)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Suchen ...",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = MaterialTheme.shapes.medium)
                    .padding(16.dp)
            )
        }

        // Filter- und Einstellungen-Buttons unten rechts
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.filter_black),
                    contentDescription = "Filter"
                )
            }

            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.settings_black),
                    contentDescription = "Einstellungen"
                )
            }
        }

        // Location Permission Dialog
        if (showLocationDialog) {
            AlertDialog(
                onDismissRequest = { showLocationDialog = false },
                title = { Text("Standort verwenden") },
                text = { Text("Möchten Sie Ihren Standort verwenden?") },
                confirmButton = {
                    TextButton(onClick = {
                        buttonText = "Sucht in der Nähe" // Text des Buttons ändern
                        showLocationDialog = false
                    }) {
                        Text("Ja")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        buttonText = "Standort deaktiviert" // Text des Buttons ändern, wenn Nein gedrückt wird
                        showLocationDialog = false
                    }) {
                        Text("Nein")
                    }
                }
            )
        }
    }
}
