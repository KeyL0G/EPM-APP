package com.example.proof_of_concept

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.proof_of_concept.Helper.hasLocationPermission
import com.example.proof_of_concept.Viewmodels.Map_Viewmodel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proof_of_concept.Viewmodels.Osmdroid_Viewmodel

@Composable
fun StartScreen(context: Context,onNavigationClick: () -> Unit) {
    val map_viewmodel: Map_Viewmodel = viewModel()
    val osmdroid_viewmodel: Osmdroid_Viewmodel = viewModel()
    var hasPermission by remember { mutableStateOf(hasLocationPermission(context)) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasPermission = isGranted }
    )

    var showFilterMenu by remember { mutableStateOf(false) }
    var buttonText by remember {
        if(!hasPermission)
            mutableStateOf("In der Nähe suchen?")
        else
            mutableStateOf("Sucht in der Nähe.")
    }

    val map by map_viewmodel.map.observeAsState(initial = null)
    val location by map_viewmodel.currentLocation.observeAsState(initial = null)

//    var selectedTab by remember { mutableStateOf(0) }
//    val toilettenListe = listOf(
//        "Straße 1234",
//        "Straße 5678",
//        "Straße 91011",
//        "Straße 121314",
//        "Straße 151617"
//    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    if (hasPermission) {
                        map_viewmodel.updateLocation(context)
                        map_viewmodel.moveMapToCurrentLocation()

                        if(map != null && location != null)
                            osmdroid_viewmodel.getToiletsFromLocation(map!!, context, location!!)
                        else
                            Log.e("WARN", "Location or Map not found")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !hasPermission
            ) {
                Text(buttonText, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Suchen ...",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = MaterialTheme.shapes.medium)
                    .padding(12.dp),
                style = MaterialTheme.typography.bodyMedium
            )

//            TabRow(
//                selectedTabIndex = selectedTab,
//                modifier = Modifier.fillMaxWidth(),
//                containerColor = Color.White
//            ) {
//                Tab(
//                    selected = selectedTab == 0,
//                    onClick = { selectedTab = 0 }
//                ) {
//                    Text(
//                        text = "Kartenansicht",
//                        modifier = Modifier.padding(8.dp),
//                        color = if (selectedTab == 0) Color.Blue else Color.Gray,
//                        style = MaterialTheme.typography.bodySmall
//                    )
//                }
//                Tab(
//                    selected = selectedTab == 1,
//                    onClick = { selectedTab = 1 }
//                ) {
//                    Text(
//                        text = "Listenansicht",
//                        modifier = Modifier.padding(8.dp),
//                        color = if (selectedTab == 1) Color.Blue else Color.Gray,
//                        style = MaterialTheme.typography.bodySmall
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            if (selectedTab == 1) {
//                // Listenansicht
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .wrapContentHeight()
//                        .background(Color(0xFFF5F5F5))
//                        .padding(8.dp)
//                ) {
//                    Text(
//                        "Köln",
//                        style = MaterialTheme.typography.bodyMedium,
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    )
//
//                    toilettenListe.forEachIndexed { index, adresse ->
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 4.dp)
//                                .background(Color.White, shape = MaterialTheme.shapes.small)
//                                .padding(12.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Column(modifier = Modifier.weight(1f)) {
//                                Text(
//                                    adresse,
//                                    style = MaterialTheme.typography.bodyMedium,
//                                    modifier = Modifier.padding(bottom = 4.dp)
//                                )
//                                Row(verticalAlignment = Alignment.CenterVertically) {
//                                    repeat(4) {
//                                        Icon(
//                                            painter = painterResource(id = R.drawable.star_filled_blue),
//                                            contentDescription = "Bewertung",
//                                            tint = Color.Blue,
//                                            modifier = Modifier.size(14.dp)
//                                        )
//                                    }
//                                    Icon(
//                                        painter = painterResource(id = R.drawable.star_outline_blue),
//                                        contentDescription = "Bewertung",
//                                        tint = Color.Blue,
//                                        modifier = Modifier.size(14.dp)
//                                    )
//                                    Spacer(modifier = Modifier.width(4.dp))
//                                    Text(
//                                        "100 Bewertungen",
//                                        style = MaterialTheme.typography.bodySmall
//                                    )
//                                }
//                            }
//                            Icon(
//                                painter = painterResource(id = R.drawable.keyboard_arrow_right_black),
//                                contentDescription = "Details anzeigen",
//                                tint = Color.Gray,
//                                modifier = Modifier.size(20.dp)
//                            )
//                        }
//
//                        if (index != toilettenListe.lastIndex) {
//                            Divider(
//                                color = Color.Gray.copy(alpha = 0.3f),
//                                thickness = 1.dp,
//                                modifier = Modifier.padding(vertical = 4.dp)
//                            )
//                        }
//                    }
//                }
//            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(
                onClick = { showFilterMenu = !showFilterMenu },
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

        if (showFilterMenu) {
            FilterMenu(
                onDismiss = { showFilterMenu = false }
            )
        }
    }
}

@Composable
fun FilterMenu(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)) // Hintergrund abdunkeln
            .clickable { onDismiss() } // Schließen bei Klick außerhalb
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White, shape = MaterialTheme.shapes.large)
                .padding(16.dp)
        ) {
            Text("Filter-Einstellungen:", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(16.dp))

            // Filter-Optionen
            val filters = listOf(
                "Rollstuhlfreundlich" to remember { mutableStateOf(false) },
                "Wickeltisch" to remember { mutableStateOf(false) },
                "Kostenlos" to remember { mutableStateOf(false) },
            )

            filters.forEach { (label, state) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = state.value,
                        onCheckedChange = { state.value = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(label)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                Text("Schließen")
            }
        }
    }
}

