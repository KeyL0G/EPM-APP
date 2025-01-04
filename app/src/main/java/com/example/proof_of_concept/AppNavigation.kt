package com.example.proof_of_concept

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "start") {
        // Startscreen
        composable("start") {
            startScreen(
                onSettingsClick = {
                    navController.navigate("settings")
                }
            )
        }

        // Settingsscreen
        composable("settings") {
            settingsScreen(
                onBackClick = {
                    navController.popBackStack() // Sicherstellen, dass popBackStack korrekt aufgerufen wird
                }
            )
        }
    }
}