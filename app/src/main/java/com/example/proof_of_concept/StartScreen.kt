package com.example.proof_of_concept

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

@Composable
fun StartScreen(onNavigationClick: () -> Unit, askLocationPermission: () -> Unit) {
    var buttonText by remember { mutableStateOf("In der Nähe suchen?") }
    var selectedTab by remember { mutableStateOf(0) }
    val toilettenListe = listOf(
        "Straße 1234",
        "Straße 5678",
        "Straße 91011",
        "Straße 121314",
        "Straße 151617"
    )
    var showFilterMenu by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Suche Button
            Button(
                onClick = {
                    buttonText = "Sucht in der Nähe."
                    askLocationPermission()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(buttonText, style = MaterialTheme.typography.bodySmall) // Kleinere Schrift
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Suchleiste
            Text(
                text = "Suchen ...",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = MaterialTheme.shapes.medium)
                    .padding(12.dp), // Kompakte, aber sichtbare Größe
                style = MaterialTheme.typography.bodyMedium // Lesbare Schriftgröße
            )

            Spacer(modifier = Modifier.height(8.dp)) // Abstand zur Ansichtsauswahl

            // Ansichtsauswahl (Karten- und Listenansicht)
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.White
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                ) {
                    Text(
                        text = "Kartenansicht",
                        modifier = Modifier.padding(8.dp),
                        color = if (selectedTab == 0) Color.Blue else Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                ) {
                    Text(
                        text = "Listenansicht",
                        modifier = Modifier.padding(8.dp),
                        color = if (selectedTab == 1) Color.Blue else Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp)) // Abstand zur Liste

            if (selectedTab == 1) {
                // Listenansicht
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight() // Höhe passt sich der Liste an
                        .background(Color(0xFFF5F5F5)) // Dezenter Hintergrund
                        .padding(8.dp)
                ) {
                    Text(
                        "Köln",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Liste der Toiletten
                    toilettenListe.forEachIndexed { index, adresse ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(Color.White, shape = MaterialTheme.shapes.small)
                                .padding(12.dp), // Angenehme Größe
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    adresse,
                                    style = MaterialTheme.typography.bodyMedium, // Etwas größere Schrift
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    repeat(4) { // 4 Sterne
                                        Icon(
                                            painter = painterResource(id = R.drawable.star_filled_blue),
                                            contentDescription = "Bewertung",
                                            tint = Color.Blue,
                                            modifier = Modifier.size(14.dp) // Kompakte Sterne
                                        )
                                    }
                                    Icon(
                                        painter = painterResource(id = R.drawable.star_outline_blue),
                                        contentDescription = "Bewertung",
                                        tint = Color.Blue,
                                        modifier = Modifier.size(14.dp) // Kompakte Sterne
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "100 Bewertungen",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                            Icon(
                                painter = painterResource(id = R.drawable.keyboard_arrow_right_black),
                                contentDescription = "Details anzeigen",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp) // Lesbarer Pfeil
                            )
                        }

                        if (index != toilettenListe.lastIndex) {
                            Divider(
                                color = Color.Gray.copy(alpha = 0.3f),
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(
                onClick = { showFilterMenu = !showFilterMenu },
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.filter_black),
                    contentDescription = "Filter"
                )
            }

            IconButton(
                onClick = onNavigationClick,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.settings_black),
                    contentDescription = "Einstellungen"
                )
            }
        }

        // Aufklappbares Fenster
        if (showFilterMenu) {
            FilterMenu(
                onDismiss = { showFilterMenu = false }
            )
        }
    }
}

@Composable
fun FilterMenu(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)) // Hintergrund abdunkeln
            .clickable { onDismiss() } // Schließen bei Klick außerhalb
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White, shape = MaterialTheme.shapes.large)
                .padding(16.dp)
        ) {
            Text("Filter-Einstellungen:", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(16.dp))

            // Filter-Optionen
            val filters = listOf(
                "Rollstuhlfreundlich" to remember { mutableStateOf(false) },
                "Wickeltisch" to remember { mutableStateOf(false) },
                "Kostenlos" to remember { mutableStateOf(false) },
            )

            filters.forEach { (label, state) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = state.value,
                        onCheckedChange = { state.value = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(label)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Schließen-Button
            Button(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                Text("Schließen")
            }
        }
    }
}

