package com.example.proof_of_concept

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun settingsScreen(onBackClick: () -> Unit) {
    // State for language dropdown and other fields
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("Deutsch") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Einstellungen",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Lautstärke
        Text("Lautstärke:")
        Slider(
            value = 0.5f,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filter-Details
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

        // Sprache
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

        // Support
        Text("Support:")
        Text("Entwickelt von K.Y.E.")
        Text("Download in Android")
        Text(
            "E-Mail: email@toLocate.de",
            color = Color.Blue,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Zurück-Button
        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Zurück")
        }
    }
}
