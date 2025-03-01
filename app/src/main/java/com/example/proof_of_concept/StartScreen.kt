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
import com.example.proof_of_concept.Api_Helper.ToiletOptions
import com.example.proof_of_concept.Viewmodels.Osmdroid_Viewmodel
import com.example.proof_of_concept.Viewmodels.ToiletDetail

@Composable
fun StartScreen(context: Context,onNavigationClick: () -> Unit, onLocationClick: () -> Unit) {
    val map_viewmodel: Map_Viewmodel = viewModel()
    val osmdroid_viewmodel: Osmdroid_Viewmodel = viewModel()
    val map by map_viewmodel.map.observeAsState(initial = null)
    val location by map_viewmodel.currentLocation.observeAsState(initial = null)
    val toilets by osmdroid_viewmodel.currentToilets.observeAsState(initial = emptyList())
    val hasPermission by map_viewmodel.hasPermission.observeAsState(initial = false)
    map_viewmodel.updatePermission(hasLocationPermission(context))
    var filterToilet by remember { mutableStateOf(toilets) }
    var selectedTab by remember { mutableStateOf(0) }
    val filters = listOf(
        "Rollstuhlfreundlich" to remember { mutableStateOf(false) },
        "Wickeltisch" to remember { mutableStateOf(false) },
        "Kostenlos" to remember { mutableStateOf(false) },
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
           map_viewmodel.updatePermission(isGranted)
            if (hasPermission) {
                map_viewmodel.updateLocation(context)
                map_viewmodel.moveMapToCurrentLocation(context)
                if(map != null && location != null)
                    osmdroid_viewmodel.getToiletsFromLocation(map!!, context, location!!)
                else
                    Log.e("WARN", "Location or Map not found")
            }
        }
    )
    var showFilterMenu by remember { mutableStateOf(false) }
    LaunchedEffect(toilets) {
        filterToilet = toilets
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !hasPermission,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                )
            ) {
                Text(text = if (location == null) "In der Nähe suchen?" else "Sucht in der Nähe.", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (filterToilet.size > 0){
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
                        modifier = Modifier.padding(8.dp),
                        color = if (selectedTab == 0) Color.Blue else Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                ) {
                    Text(
                        text = "Listenansicht",
                        modifier = Modifier.padding(8.dp),
                        color = if (selectedTab == 1) Color.Blue else Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (selectedTab == 1) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(Color(0xFFF5F5F5))
                        .padding(8.dp)
                ) {
                    filterToilet.forEachIndexed { index, toilet ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(Color.White, shape = MaterialTheme.shapes.small)
                                .padding(12.dp)
                                .clickable {
                                    osmdroid_viewmodel.setCurrentSelectedToilet(toilet)
                                    onLocationClick()
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = toilet.fullAddress,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    repeat(4) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.star_filled_blue),
                                            contentDescription = "Bewertung",
                                            tint = Color.Blue,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Icon(
                                        painter = painterResource(id = R.drawable.star_outline_blue),
                                        contentDescription = "Bewertung",
                                        tint = Color.Blue,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "100 Bewertungen",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                            Icon(
                                painter = painterResource(id = R.drawable.keyboard_arrow_right_black),
                                contentDescription = "Details anzeigen",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    }
                }
            }
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
                activeFilter = filters,
                onDismiss = { showFilterMenu = false },
                setFilterToilets = {filterToilet = getFilteredToiletFromLocation(filterToilet,it) }

            )
        }
    }
}

fun getFilteredToiletFromLocation(currentToilets: List<ToiletDetail>,toiletOptions: List<ToiletOptions>): List<ToiletDetail> {
   return when(toiletOptions.size){
        1 ->  currentToilets.filter { it.options.contains(toiletOptions[0])}
        2 ->  currentToilets.filter { it.options.contains(toiletOptions[0]) || it.options.contains(toiletOptions[1])}
        3 ->  currentToilets.filter { it.options.contains(toiletOptions[0]) || it.options.contains(toiletOptions[1]) || it.options.contains(toiletOptions[2])}
       else -> currentToilets
    }

}

@Composable
fun FilterMenu(activeFilter :  List<Pair<String, MutableState<Boolean>>>,setFilterToilets: (currentToiletOptions: List<ToiletOptions>) -> Unit, onDismiss: () -> Unit) {
    val osmdroid_viewmodel: Osmdroid_Viewmodel = viewModel()
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

            val filters = activeFilter

            filters.forEach { (label, state) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = state.value,
                        onCheckedChange = {
                            state.value = it
                            setFilterToilets(getFiltersAsToiletOptions(filters))
                        }
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
fun getFiltersAsToiletOptions(filter: List<Pair<String, MutableState<Boolean>>>): List<ToiletOptions>{
    val toiletOptions: MutableList<ToiletOptions> = mutableListOf()
    filter.forEach{
            (label, state) ->
        if (label == "Wickeltisch" && state.value) toiletOptions.add(ToiletOptions.CHANGING_TABLE)
        if (label == "Rollstuhlfreundlich" && state.value) toiletOptions.add(ToiletOptions.WHEELCHAIR)
        if (label == "Kostenlos" && state.value) toiletOptions.add(ToiletOptions.FEE)
    }
    return toiletOptions
}
