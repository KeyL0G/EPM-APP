package com.example.proof_of_concept

import androidx.compose.runtime.Composable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proof_of_concept.Viewmodels.Map_Viewmodel
import com.example.proof_of_concept.Viewmodels.Osmdroid_Viewmodel
import com.example.proof_of_concept.Viewmodels.ToiletDetail
@Composable
fun Bewertung() {

    val map_viewmodel: Map_Viewmodel = viewModel()
    val osmdroid_viewmodel: Osmdroid_Viewmodel = viewModel()
    val selectedToilet by osmdroid_viewmodel.currentSelectedToilet.observeAsState(initial = ToiletDetail())
    val currentLocation by map_viewmodel.currentLocation.observeAsState(initial = null)
    var comment by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Button(
                onClick = {},
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
                    Text(text = "Route")
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = selectedToilet.fullAddress,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Color.Black))

                    X(rating = 4.5f, 50.dp)

                    Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Color.Black))

                    Text(
                        text = "Details:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(5.dp).align(Alignment.Start)
                    )
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Text("Kostenlos:")

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
                                Text("Ja")
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
                                Text("Nein")
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Barrierefrei:")

                            X(rating = 4.5f, 30.dp)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Ausstatung:")
                            X(rating = 4.5f, 30.dp)
                        }
                    }

                    Row(
                        modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 10.dp).align(Alignment.Start)
                    ) {

                        selectedToilet.options.forEach { it ->
                            Text(
                                text = it,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.background(Color.Blue).padding(5.dp),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Color.Black))

                    Text(
                        text = "Kommentar abgeben:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(5.dp).align(Alignment.Start)
                    )
                    TextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text("BETREFF") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextField(
                        value = comment,
                        onValueChange = { comment = it },
                        label = { Text("Kommentar schreiben...") },
                        modifier = Modifier.fillMaxWidth().defaultMinSize(50.dp, 100.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp).fillMaxWidth())
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                            Text("Abschicken")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun X(rating: Float, size : Dp ) {

    Row(modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)) {
        repeat(5) { index ->
            Image(
                painter = painterResource(
                    id = if (index < rating.toInt()) R.drawable.star_filled_blue else R.drawable.star_outline_blue
                ),
                contentDescription = null,
                modifier = Modifier.size(size)
            )
        }
    }
}
















//                val osmdroidViewModel: Osmdroid_Viewmodel = viewModel()
//                val selectedToilet by osmdroidViewModel.currentSelectedToilet.observeAsState(initial = ToiletDetail())
//                var rating by remember { mutableStateOf(0) }
//                var comment by remember { mutableStateOf("") }
//                var subject by remember { mutableStateOf("") }
//
//                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//                    Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(Color.LightGray)) {
//
//                        Text("Kartenansicht", modifier = Modifier.align(Alignment.Center), fontSize = 18.sp)
//                    }
//
//                    Text(text = selectedToilet.fullAddress, fontSize = 20.sp, modifier = Modifier.padding(top = 8.dp))
//
//                    RatingBar(rating) { newRating -> rating = newRating }
//                    Button(onClick = { /* Bewertung absenden */ }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
//                        Text("Bewertung abgeben")
//                    }
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    DetailSection(selectedToilet.options)
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Text("Kommentar abgeben:", fontSize = 18.sp)
//                    TextField(value = subject, onValueChange = { subject = it }, label = { Text("BETREFF") })
//                    TextField(
//                        value = comment,
//                        onValueChange = { comment = it },
//                        label = { Text("Kommentar schreiben...") },
//                        modifier = Modifier.height(100.dp)
//                    )
//
//                    Button(onClick = onSubmitReview, modifier = Modifier.align(Alignment.CenterHorizontally)) {
//                        Text("Abschicken")
//                    }
//                }
//            }
//
//    @Composable
//    fun RatingBar(rating: Int, onRatingChange: (Int) -> Unit) {
//        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
//            repeat(5) { index ->
//                val isSelected = index < rating
//                Image(
//                    painter = painterResource(
//                        id = if (isSelected) R.drawable.star_filled_blue else R.drawable.star_outline_blue
//                    ),
//                    contentDescription = null,
//                    modifier = Modifier.size(40.dp).padding(4.dp)
//                )
//            }
//        }
//    }
//
//    @Composable
//    fun DetailSection(options: List<String>) {
//        Column(modifier = Modifier.fillMaxWidth()) {
//            Text("Details:", fontSize = 18.sp)
//            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                options.forEach { option ->
//                    Button(
//                        onClick = {},
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White)
//                    ) {
//                        Text(option)
//                    }
//                }
//            }
//        }
