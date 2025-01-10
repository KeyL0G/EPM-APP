package com.example.proof_of_concept

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.osmdroid.views.MapView

@Composable
fun Main_App(map: MapView?, context: Context, askLocationPermission: () -> Unit) {
    var selectedNavigation by remember{ mutableStateOf("Start") }

    when(selectedNavigation){
        "Start" -> StartScreen(
            onNavigationClick = {
                    selectedNavigation = "Settings"
                },
                askLocationPermission = {
                    askLocationPermission()
                }
            )

        "Settings" -> SettingsScreen(
            onNavigationClick = {
                    selectedNavigation = "Start"
                }
            )
    }

}