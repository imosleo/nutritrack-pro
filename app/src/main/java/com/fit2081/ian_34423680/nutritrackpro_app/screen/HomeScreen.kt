@file:OptIn(ExperimentalMaterial3Api::class)

package com.fit2081.ian_34423680.nutritrackpro_app.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fit2081.ian_34423680.nutritrackpro_app.R
import com.fit2081.ian_34423680.nutritrackpro_app.navigation.ScreenRoute
import com.fit2081.ian_34423680.nutritrackpro_app.screen.ui.AppBottomBar
import com.fit2081.ian_34423680.nutritrackpro_app.utils.SessionManager
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.HomeViewModel
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.PatientViewModel


@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel,
    sessionManager: SessionManager,
    patientViewModel: PatientViewModel
) {
    val userId by viewModel.userId.collectAsState()
    val userName by viewModel.userName.collectAsState() // <-- Collect userName
    val foodScore by viewModel.foodScore.collectAsState()

    // Load user data and score once on entering the screen
    LaunchedEffect(Unit) {

        // Save the last visited screen in the session
        sessionManager.saveSession(
            userId = sessionManager.getLoggedUser() ?: "Unknown",
            lastRoute = ScreenRoute.Home.route
        )
    }

    Scaffold(
        bottomBar = { AppBottomBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Text("Hello,", fontSize = 16.sp, color = Color.Gray)
            Text("$userName ($userId)", fontWeight = FontWeight.Bold, fontSize = 28.sp)
            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "You've already filled in your Food Intake Questionnaire, but you can change details here:",
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        navController.navigate("questionnaire") {
                            popUpTo("home") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B1FA2)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(4.dp))
                    Text("Edit", color = Color.White)
                }
            }

            Spacer(Modifier.height(20.dp))

            Image(
                painter = painterResource(R.drawable.food_plate),
                contentDescription = "Food Plate",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("My Score", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    text = "See all scores >",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        navController.navigate("insights")
                    }
                )
            }

            Spacer(Modifier.height(12.dp))

            val scoreValue = foodScore.substringBefore("/").toFloatOrNull()
            val arrowIcon = if (scoreValue != null && scoreValue < 50f) {
                Icons.Default.ArrowDownward
            } else {
                Icons.Default.ArrowUpward
            }
            val scoreColor = if (scoreValue != null && scoreValue < 50f) {
                Color.Red
            } else {
                Color(0xFF008000) // Green
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(arrowIcon, contentDescription = null, tint = scoreColor) // <-- Set tint here
                Spacer(Modifier.width(8.dp))
                Text("Your Food Quality Score", fontSize = 16.sp)
                Spacer(Modifier.weight(1f))
                Text(
                    foodScore,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (foodScore != "N/A") scoreColor else Color.Gray
                )
            }

            Spacer(Modifier.height(24.dp))

            Text("What is the Food Quality Score?", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Your Food Quality Score provides a snapshot of how well your eating patterns align with established food guidelines, helping you identify both strengths and opportunities for improvement in your diet.\n\nThis personalized measurement considers various food groups including vegetables, fruits, whole grains, and proteins to give you practical insights for making healthier food choices.",
                fontSize = 14.sp
            )
        }
    }
}