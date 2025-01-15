package com.example.proof_of_concept

import android.Manifest
import android.content.Context
import android.widget.Button
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proof_of_concept.Viewmodels.Map_Viewmodel
import com.example.proof_of_concept.Viewmodels.Navigation_Page
import org.osmdroid.views.MapView

@Composable
fun Main_App(context: Context) {
    val map_viewmodel: Map_Viewmodel = viewModel()
    var currentRoute = map_viewmodel.currentRoute.observeAsState(initial = null)
    val selectedNavigation by map_viewmodel.navigationPage.observeAsState(initial = Navigation_Page.START)

    when(selectedNavigation){
        Navigation_Page.START -> StartScreen(
            context = context,
            onNavigationClick = {
                map_viewmodel.updateNavigationPage(Navigation_Page.SETTINGS)
                },
            onLocationClick = {
                   map_viewmodel.updateNavigationPage(Navigation_Page.LOCATIONDESCRIPTION)
                }
            )

        Navigation_Page.SETTINGS -> SettingsScreen(
            onNavigationClick = {
                map_viewmodel.updateNavigationPage(Navigation_Page.START)
                }
            )

        Navigation_Page.LOCATIONDESCRIPTION-> LocationDescription(
            onBackNavigation = {
                map_viewmodel.updateNavigationPage(Navigation_Page.START)

            },
            onGoNavigation = {
                map_viewmodel.updateNavigationPage(Navigation_Page.LOCATIONNAVIGATION)
            }
        )

        Navigation_Page.LOCATIONNAVIGATION -> LocationNavigation(
            onBackNavigation = {
                map_viewmodel.deleteRouteFromMap(currentRoute.value)
                map_viewmodel.updateNavigationPage(Navigation_Page.LOCATIONDESCRIPTION)
            },
            onGoNavigation = {
                map_viewmodel.updateNavigationPage(Navigation_Page.STARTROUTE)
            }
        )

        Navigation_Page.STARTROUTE -> startRoute(
            onNavigationClick = {
                map_viewmodel.updateNavigationPage(Navigation_Page.LOCATIONNAVIGATION)
            }
        )

    }

}

@Composable
fun startRoute(onNavigationClick: () -> Unit){
    Button(
        onClick = onNavigationClick,

        ){

    }
}