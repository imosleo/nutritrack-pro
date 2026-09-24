package com.fit2081.ian_34423680.nutritrackpro_app.screen

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fit2081.ian_34423680.nutritrackpro_app.screen.ui.AppBottomBar
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.InsightViewModel
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.PatientViewModel

@Composable
fun InsightsScreen(
    navController: NavHostController,
    patientViewModel: PatientViewModel,
    insightViewModel: InsightViewModel
) {
    val context = LocalContext.current

    val currentPatient = patientViewModel.currentPatient
    val scoreMap by insightViewModel.heifaScores.collectAsState()
    val totalScore by insightViewModel.totalScore.collectAsState()

    LaunchedEffect(currentPatient?.userId) {
        currentPatient?.userId?.let { userId ->
            insightViewModel.loadByUserId(userId.toString())        }
    }

    val maxScores = mapOf(
        "Water" to 5f, "Alcohol" to 5f,
        "Whole Grains" to 5f, "Saturated Fats" to 5f, "Unsaturated Fats" to 5f
    )

    Scaffold(
        bottomBar = { AppBottomBar(navController) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Text("Insights: Food Score", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
            }

            items(scoreMap.entries.toList()) { (category, score) ->
                val max = maxScores[category] ?: 10f
                Column(modifier = Modifier.padding(vertical = 2.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(category, modifier = Modifier.weight(1f), fontSize = 14.sp)
                        Text(String.format("%.1f/%.0f", score, max), fontSize = 14.sp)
                    }
                    Slider(
                        value = score.coerceAtMost(max),
                        onValueChange = {},
                        valueRange = 0f..max,
                        steps = (max - 1).toInt().coerceAtLeast(0),
                        enabled = false,
                        colors = SliderDefaults.colors(
                            disabledThumbColor = Color(0xFF7B1FA2),
                            disabledActiveTrackColor = Color(0xFF7B1FA2),
                            disabledInactiveTrackColor = Color(0xFFD1B3E0)
                        ),
                        modifier = Modifier.height(24.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Total Food Quality Score", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                val totalVal = totalScore ?: 0f
                Slider(
                    value = totalVal,
                    onValueChange = {},
                    valueRange = 0f..100f,
                    steps = 99,
                    enabled = false,
                    colors = SliderDefaults.colors(
                        disabledThumbColor = Color.Transparent,
                        disabledActiveTrackColor = Color(0xFF7B1FA2),
                        disabledInactiveTrackColor = Color(0xFFD1B3E0)
                    ),
                    modifier = Modifier.height(24.dp)
                )
                Text(String.format("%.1f/100", totalVal), fontSize = 16.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val msg = "Hi, I just got a HEIFA score of ${totalVal.toInt()}!"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, msg)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share your HEIFA score via:"))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Icon(Icons.Filled.Share, null, tint = Color.White)
                    Spacer(Modifier.width(6.dp))
                    Text("Share with someone", color = Color.White)
                }

                Button(
                    onClick = { navController.navigate("nutricoach") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Icon(Icons.Filled.ThumbUp, null, tint = Color.White)
                    Spacer(Modifier.width(6.dp))
                    Text("Improve my diet!", color = Color.White)
                }

                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}


