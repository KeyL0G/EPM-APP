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
