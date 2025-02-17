package com.example.proof_of_concept

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proof_of_concept.Api_Helper.Route
import com.example.proof_of_concept.Viewmodels.Map_Viewmodel
import com.example.proof_of_concept.Viewmodels.Osmdroid_Viewmodel


@Composable
fun LocationNavigation(onBackNavigation: () -> Unit, onGoNavigation: () -> Unit) {
    val map_viewmodel: Map_Viewmodel = viewModel()
    val osmdroid_viewmodel: Osmdroid_Viewmodel = viewModel()
    val allRoutesFromAPI by osmdroid_viewmodel.allRoutesFromAPI.observeAsState(initial = null)
    var activeRoute by remember { mutableStateOf(0) }
    var showStepsView by remember { mutableStateOf(false) }

    if (allRoutesFromAPI != null) {
        when (activeRoute) {
            0 -> {
                map_viewmodel.updateRoute(allRoutesFromAPI!!.Car.route[0].geometry)
                map_viewmodel.drawRoute(allRoutesFromAPI!!.Car.route[0].geometry)
                map_viewmodel.updateCurrentSteps(allRoutesFromAPI!!.Car.route[0].steps)
            }
            1 -> {
                map_viewmodel.updateRoute(allRoutesFromAPI!!.Foot.route[0].geometry)
                map_viewmodel.drawRoute(allRoutesFromAPI!!.Foot.route[0].geometry)
                map_viewmodel.updateCurrentSteps(allRoutesFromAPI!!.Foot.route[0].steps)
            }
            2 -> {
                map_viewmodel.updateRoute(allRoutesFromAPI!!.Bike.route[0].geometry)
                map_viewmodel.drawRoute(allRoutesFromAPI!!.Bike.route[0].geometry)
                map_viewmodel.updateCurrentSteps(allRoutesFromAPI!!.Bike.route[0].steps)

            }
            3 -> {
                map_viewmodel.updateRoute(allRoutesFromAPI!!.Access.route[0].geometry)
                map_viewmodel.drawRoute(allRoutesFromAPI!!.Access.route[0].geometry)
                map_viewmodel.updateCurrentSteps(allRoutesFromAPI!!.Access.route[0].steps)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Deine Buttons für Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onBackNavigation,
                    modifier = Modifier.size(width = 100.dp, height = 40.dp),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Blue
                    ),
                    border = BorderStroke(1.dp, Color.Blue)
                ) {
                    Text(text = "Zurück")
                }

                Button(
                    onClick = onGoNavigation,
                    modifier = Modifier.size(width = 100.dp, height = 40.dp),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Start")
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Auswahl der Route
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    listOf(
                        Pair(R.drawable.car_blue, R.drawable.car_black) to "10 min",
                        Pair(R.drawable.footprint_blue, R.drawable.footprint_black) to "10 min",
                        Pair(R.drawable.bike_blue, R.drawable.bike_black) to "10 min",
                        Pair(R.drawable.accessible_blue, R.drawable.accessible_black) to "10 min"
                    ).forEachIndexed { index, (icons, text) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(onClick = { activeRoute = index }) {
                                Image(
                                    painter = painterResource(
                                        id = if (activeRoute == index) icons.first else icons.second
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                            Text(text)
                        }
                    }
                }

                // Buttons für Optionen
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = { showStepsView = true }) {
                            Image(
                                painter = painterResource(id = R.drawable.route_black),
                                contentDescription = null,
                                modifier = Modifier.size(40.dp).rotate(90f),
                                contentScale = ContentScale.Inside
                            )
                        }
                        Text("Steps")
                    }

                    Button(
                        onClick = {},
                        modifier = Modifier.size(110.dp, 30.dp),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Blue,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Kürzeste Weg")
                    }

                    Button(
                        onClick = {},
                        modifier = Modifier.size(110.dp, 30.dp),
                        shape = RoundedCornerShape(2.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Blue
                        ),
                        border = BorderStroke(1.dp, Color.Blue)
                    ) {
                        Text("Schnellste Weg")
                    }
                }

                Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Color.Black))

                listOf("Von:" to "Straße123", "Nach:" to "Straße123").forEach { (label, value) ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(0.dp, 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(label, modifier = Modifier.width(40.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(value)
                        }
                    }
                    Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Color.Black.copy(alpha = 0.2f)))
                }
            }
        }

        if (showStepsView) {
            if (allRoutesFromAPI != null) {
                when (activeRoute) {

                    0 -> {
                        stepsUI(allRoutesFromAPI!!.Car.route[0]) { showStepsView = false }
                    }
                    1 -> {
                        stepsUI(allRoutesFromAPI!!.Foot.route[0]) { showStepsView = false }
                    }
                    2 -> {
                        stepsUI(allRoutesFromAPI!!.Bike.route[0]) { showStepsView = false }
                    }
                    3 -> {
                        stepsUI(allRoutesFromAPI!!.Access.route[0]) { showStepsView = false }
                    }
                }
            }
        }
    }
}


@Composable
fun stepsUI(activeRoute: Route, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .background(Color.White, shape = MaterialTheme.shapes.large)
                .padding(16.dp)
        ) {
            for (i in 1 until activeRoute.steps.size) {
            Column (verticalArrangement = Arrangement.SpaceAround){
                Row(
                    modifier = Modifier.fillMaxWidth().padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                        Text(
                            text = "$i.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Text(
                                text = activeRoute.steps[i - 1].instructions,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Text(
                                text = activeRoute.steps[i - 1].name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Black
                            )
                        }

                    }
                }
            }
        }
    }
}