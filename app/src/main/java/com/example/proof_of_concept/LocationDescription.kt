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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LocationDescription() {
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
                onClick = { /* Handle "Zurück" click */ },
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
                onClick = { /* Handle "Route" click */ },
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
                    .background(Color.White)
                ,
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Straße 123",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Color.Black))

                    RatingBar(rating = 4.5f)
                    Text(
                        text = "100 Bewertungen",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Color.Black))

                    Text(
                        text = "Details:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(5.dp).align(Alignment.Start)
                    )

                    Row(modifier = Modifier.padding(5.dp, 0.dp,0.dp,10.dp).align(Alignment.Start)) {
                        Text(
                            text = "Kostenlos",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.background(Color.Blue).padding(5.dp),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Barrierefrei",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.background(Color.Blue).padding(5.dp),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Wickelstation",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.background(Color.Blue).padding(5.dp),
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Color.Black))

                    Text(
                        text = "Kommentare:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(5.dp).align(Alignment.Start)
                    )
                    Text(
                        text="Kommentar Kommentar Kommentar Kommentar Kommentar Kommentar Kommentar Kommentar Kommentar",
                        modifier = Modifier.padding(5.dp).align(Alignment.Start),
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Color.Black.copy(alpha = 0.2f)))

                    Text(
                        text="Kommentar Kommentar Kommentar Kommentar Kommentar Kommentar Kommentar Kommentar Kommentar",
                        modifier = Modifier.padding(5.dp).align(Alignment.Start),
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Color.Black.copy(alpha = 0.2f)))

                    Text(
                        text="Kommentar Kommentar Kommentar Kommentar Kommentar Kommentar Kommentar Kommentar Kommentar",
                        modifier = Modifier.padding(5.dp).align(Alignment.Start),
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun RatingBar(rating: Float) {
    Row(modifier = Modifier.padding(0.dp,10.dp,0.dp,0.dp)) {
        repeat(5) { index ->
            Image(
                painter = painterResource(
                    id = if (index < rating.toInt()) R.drawable.star_filled_blue else R.drawable.star_outline_blue
                ),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
        }
    }
}

