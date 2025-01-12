package com.example.proof_of_concept

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.proof_of_concept.Viewmodels.Map_Viewmodel
import com.example.proof_of_concept.Viewmodels.Osmdroid_Viewmodel


@Composable
fun LocationNavigation(onBackNavigation: () -> Unit, onGoNavigation: () -> Unit) {
    val map_viewmodel: Map_Viewmodel = viewModel()
    val osmdroid_viewmodel: Osmdroid_Viewmodel = viewModel()
    val routes by osmdroid_viewmodel.routes.observeAsState(initial = null)
    var activeRoute by remember { mutableStateOf(0) }

    if (routes != null) {
        when (activeRoute) {
            0 -> {
                map_viewmodel.updateRoute(routes!!.routeCar[0])
                map_viewmodel.drawRoute(routes!!.routeCar[0])} //car
            1 -> {
                map_viewmodel.updateRoute(routes!!.routeFoot[0])
                map_viewmodel.drawRoute(routes!!.routeFoot[0])}//foot
            2 -> {
                map_viewmodel.updateRoute(routes!!.routeBike[0])
                map_viewmodel.drawRoute(routes!!.routeBike[0])}//bike
            3 -> {
                map_viewmodel.updateRoute(routes!!.routeAccess[0])
                map_viewmodel.drawRoute(routes!!.routeAccess[0])} //access
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Zurück")
                    }
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Start")
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = { activeRoute = 0 }) {
                            Image(
                                painter = painterResource(id =
                                   if(activeRoute == 0) R.drawable.car_blue else R.drawable.car_black
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(35.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Text("10 min")
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = { activeRoute = 1 }) {
                            Image(
                                painter = painterResource(id =
                                    if(activeRoute == 1) R.drawable.footprint_blue else R.drawable.footprint_black
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(35.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Text("10 min")
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = { activeRoute = 2 }) {
                            Image(
                                painter = painterResource(id =
                                    if(activeRoute == 2) R.drawable.bike_blue else R.drawable.bike_black
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(35.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Text("10 min")
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = { activeRoute = 3 }) {
                            Image(
                                painter = painterResource(id =
                                    if(activeRoute == 3) R.drawable.accessible_blue else R.drawable.accessible_black
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(35.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Text("10 min")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = {}) {
                            Image(
                                painter = painterResource(id = R.drawable.route_black),
                                contentDescription = null,
                                modifier = Modifier.size(40.dp).rotate(90f),
                                contentScale = ContentScale.Inside
                            )
                        }
                        Text("Route")
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

                Row(
                    modifier = Modifier.fillMaxWidth().padding(0.dp,10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Von:", modifier = Modifier.width(40.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Straße123")
                    }
                }

                Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Color.Black.copy(alpha = 0.2f)))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(0.dp,10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Nach:", modifier = Modifier.width(40.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Straße123")
                    }
                }
            }
        }
    }
}