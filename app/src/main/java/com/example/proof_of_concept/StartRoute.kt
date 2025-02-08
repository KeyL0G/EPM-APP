package com.example.proof_of_concept

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StartRoute(onNavigationClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Navigation Richtung ändern */ }) {
                Icon(Icons.Default.Close, contentDescription = "Richtungspfeil")
            }
            Text(text = "Beispiel - Straße", fontWeight = FontWeight.Bold)
            IconButton(onClick = { /* Refresh Funktion */ }) {
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
}