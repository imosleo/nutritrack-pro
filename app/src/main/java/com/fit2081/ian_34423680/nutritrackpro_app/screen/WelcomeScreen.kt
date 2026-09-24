package com.fit2081.ian_34423680.nutritrackpro_app.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fit2081.ian_34423680.nutritrackpro_app.R
import com.fit2081.ian_34423680.nutritrackpro_app.utils.SessionManager
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.PatientViewModel
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.QuestionnaireViewModel

@Composable
fun WelcomeScreen(navController: NavHostController,patientViewModel: PatientViewModel, questionnaireViewModel: QuestionnaireViewModel) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    var showUI by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        SessionManager.initialize(context)

        if (SessionManager.isLoggedIn()) {
            val userId = SessionManager.getLoggedUser()
            val lastRoute = SessionManager.getLastRoute()

            if (!userId.isNullOrBlank()) {
                patientViewModel.setCurrentPatient(userId)
                questionnaireViewModel.load(userId)
            }

            if (!lastRoute.isNullOrBlank() && lastRoute != "welcome" && lastRoute != "login") {
                navController.navigate(lastRoute) {
                    popUpTo("welcome") { inclusive = true }
                }
            } else {
                navController.navigate("questionnaire") {
                    popUpTo("welcome") { inclusive = true }
                }
            }
        } else {
            showUI = true
        }
    }


    if (showUI) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("NutriTrack Pro", fontSize = 32.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)

            Spacer(Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.nutritrackpro_app_logo),
                contentDescription = "NutriTrack Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "This app provides general health and nutrition information for educational purposes only.\n" +
                        "It is not intended as medical advice, diagnosis, or treatment. Always consult a qualified healthcare professional.",
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Need professional help? Book via Monash's Nutrition Clinic:",
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            TextButton(onClick = {
                uriHandler.openUri("https://www.monash.edu/medicine/scs/nutrition/clinics/nutrition")
            }) {
                Text(
                    "https://monash.edu/medicine/scs/nutrition/clinics/nutrition",
                    color = Color(0xFF3F51B5),
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(28.dp))

            Button(
                onClick = { navController.navigate("login") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login", color = Color.White)
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("register") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register", color = Color.White)
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Designed with ❤️ by Ian Leong (34423680)",
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}


