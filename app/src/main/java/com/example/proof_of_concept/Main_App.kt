package com.example.proof_of_concept

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.osmdroid.views.MapView

@Composable
fun Main_App(context: Context) {
    var selectedNavigation by remember{ mutableStateOf("Start") }

    when(selectedNavigation){
        "Start" -> StartScreen(
            context = context,
            onNavigationClick = {
                    selectedNavigation = "Settings"
                },
            onLocationClick = {
                    selectedNavigation = "locationDescription"
                }
            )

        "Settings" -> SettingsScreen(
            onNavigationClick = {
                    selectedNavigation = "Start"
                }
            )

        "locationDescription" -> LocationDescription(
            onBackNavigation = {
                selectedNavigation = "Start"
            },
            onGoNavigation = {
                selectedNavigation = "locationNavigation"
            }
        )

        "locationNavigation" -> LocationNavigation(
            onBackNavigation = {
                selectedNavigation = "locationDescription"
            },
            onGoNavigation = {
                selectedNavigation = ""
            }
        )
    }

}