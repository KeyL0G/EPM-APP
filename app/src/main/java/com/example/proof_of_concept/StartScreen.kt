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

@Composable
fun StartScreen(onNavigationClick: () -> Unit) {
    var buttonText by remember { mutableStateOf("In der Nähe suchen?") }
    var selectedTab by remember { mutableStateOf(0) } // 0 = Kartenansicht, 1 = Listenansicht
    val toilettenListe = listOf(
        "Straße 1234",
        "Straße 5678",
        "Straße 91011",
        "Straße 121314",
        "Straße 151617"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Suche Button
            Button(
                onClick = { buttonText = "Sucht in der Nähe." },
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

      

        // Icons unten rechts
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(
                onClick = {},
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
    }
}
