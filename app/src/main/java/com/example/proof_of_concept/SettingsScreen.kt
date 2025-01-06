package com.example.proof_of_concept

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(onNavigationClick: () -> Unit) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("Deutsch") }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Einstellungen",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text("Lautstärke:")
            Slider(
                value = 0.5f,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Filter-Details:")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = false, onCheckedChange = {})
                Spacer(modifier = Modifier.width(8.dp))
                Text("Barrierefrei")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = false, onCheckedChange = {})
                Spacer(modifier = Modifier.width(8.dp))
                Text("Kostenlos")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = false, onCheckedChange = {})
                Spacer(modifier = Modifier.width(8.dp))
                Text("Wickelstation")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = false, onCheckedChange = {})
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ampelfreier Weg")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Sprache:")
            Box {
                TextButton(
                    onClick = { isDropdownExpanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(selectedLanguage)
                }
                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Deutsch") },
                        onClick = {
                            selectedLanguage = "Deutsch"
                            isDropdownExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Englisch") },
                        onClick = {
                            selectedLanguage = "Englisch"
                            isDropdownExpanded = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Support:")
            Text("Entwickelt von K.Y.E.")
            Text("Download in Android")
            Text(
                "E-Mail: email@toLocate.de",
                color = Color.Blue,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigationClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Zurück")
            }
        }
    }
}
