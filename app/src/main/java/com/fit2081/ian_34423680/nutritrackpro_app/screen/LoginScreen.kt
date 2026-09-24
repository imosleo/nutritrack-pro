@file:OptIn(ExperimentalMaterial3Api::class)

package com.fit2081.ian_34423680.nutritrackpro_app.screen

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fit2081.ian_34423680.nutritrackpro_app.navigation.ScreenRoute
import com.fit2081.ian_34423680.nutritrackpro_app.utils.SessionManager
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.LoginViewModel
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.PatientViewModel
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.QuestionnaireViewModel
import androidx.core.content.edit

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel,
    sessionManager: SessionManager,
    patientViewModel: PatientViewModel,
    questionnaireViewModel: QuestionnaireViewModel
) {
    val allUserIds = viewModel.allUserIds
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }
    var selectedUserId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoggingIn by remember { mutableStateOf(false) }

    // Handle login result
    LaunchedEffect(viewModel.loginResult.value) {
        val result = viewModel.loginResult.value
        if (result != null) {
            isLoggingIn = false
            if (result) {
                patientViewModel.setCurrentPatient(selectedUserId)

                //  Load questionnaire data
                questionnaireViewModel.load(selectedUserId)
                val persona = questionnaireViewModel.persona.value
                val hasAnswered = persona.isNotBlank()

                //  Save both screen + questionnaire flag
                sessionManager.saveSession(
                    userId = selectedUserId,
                    lastRoute = if (hasAnswered) ScreenRoute.Home.route else ScreenRoute.Questionnaire.route
                )
                // Save questionnaireDone status explicitly
                context.getSharedPreferences("nutritrack_preferences", Context.MODE_PRIVATE)
                    .edit {
                        putBoolean("questionnaireDone", hasAnswered)
                        putBoolean("isLoggedIn", true)
                        putString("userId", selectedUserId)
                    }

                navController.navigate(
                    if (hasAnswered) ScreenRoute.Home.route else ScreenRoute.Questionnaire.route
                ) {
                    popUpTo(ScreenRoute.Login.route) { inclusive = true }
                }
            } else {
                errorMessage = "Invalid ID or password."
            }
            viewModel.loginResult.value = null
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Log in", fontSize = 28.sp)
        Spacer(Modifier.height(24.dp))

        // Dropdown for User ID
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                readOnly = true,
                value = selectedUserId,
                onValueChange = {},
                label = { Text("My ID (Provided by your Clinician)") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                allUserIds.forEach { id ->
                    DropdownMenuItem(
                        text = { Text(id) },
                        onClick = {
                            selectedUserId = id
                            errorMessage = ""
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Password input
        TextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = ""
            },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "This app is only for pre-registered users. Please enter your ID and password or Register to claim your account.",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (selectedUserId.isBlank() || password.isBlank()) {
                    errorMessage = "Please enter both ID and password."
                } else if (password.length < 7) {
                    errorMessage = "Password must be at least 7 characters long."
                } else {
                    isLoggingIn = true
                    viewModel.login(selectedUserId, password)
                }
            },
            enabled = !isLoggingIn,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text("Continue", color = Color.White)
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate(ScreenRoute.Register.route) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text("Register", color = Color.White)
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(errorMessage, color = Color.Red)
        }

        TextButton(
            onClick = { navController.navigate("reset") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Forgot your name or password?", fontSize = 12.sp, color = Color.Gray)
        }
    }
}