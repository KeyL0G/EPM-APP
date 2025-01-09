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
                .padding(16.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Suche Button
            Button(
                onClick = { buttonText = "Sucht in der Nähe." },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(buttonText)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Suchleiste
            Text(
                text = "Suchen ...",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = MaterialTheme.shapes.medium)
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                        modifier = Modifier.padding(16.dp),
                        color = if (selectedTab == 0) Color.Blue else Color.Gray
                    )
                }
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                ) {
                    Text(
                        text = "Listenansicht",
                        modifier = Modifier.padding(16.dp),
                        color = if (selectedTab == 1) Color.Blue else Color.Gray
                    )
                }
            }
        }

        // Inhalt basierend auf der ausgewählten Ansicht
        if (selectedTab == 1) {
            // Listenansicht
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .align(Alignment.TopCenter)
            ) {
                Text("Köln", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(8.dp))

                // Liste der Toiletten
                toilettenListe.forEach { adresse ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(Color.White, shape = MaterialTheme.shapes.medium)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(adresse, style = MaterialTheme.typography.bodyMedium)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(4) { // 4 Sterne
                                    Icon(
                                        painter = painterResource(id = R.drawable.star_filled_blue),
                                        contentDescription = "Bewertung",
                                        tint = Color.Blue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Icon(
                                    painter = painterResource(id = R.drawable.star_outline_blue),
                                    contentDescription = "Bewertung",
                                    tint = Color.Blue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text("100 Bewertungen", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Icon(
                            painter = painterResource(id = R.drawable.keyboard_arrow_right_black),
                            contentDescription = "Details anzeigen",
                            tint = Color.Gray
                        )
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
                onClick = onNavigationClick,
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
    }
}
