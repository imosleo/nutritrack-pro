@file:OptIn(ExperimentalMaterial3Api::class)

package com.fit2081.ian_34423680.nutritrackpro_app.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fit2081.ian_34423680.nutritrackpro_app.navigation.ScreenRoute
import com.fit2081.ian_34423680.nutritrackpro_app.utils.SessionManager
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.PatientViewModel

fun isValidPassword(password: String): Boolean {
    return password.any { it.isUpperCase() } &&
            password.any { it.isLowerCase() } &&
            password.any { it.isDigit() } &&
            password.any { !it.isLetterOrDigit() } &&
            password.length >= 7
}

@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: PatientViewModel
) {
    val context = LocalContext.current
    val sessionManager = SessionManager
    LaunchedEffect(Unit) {
        viewModel.refreshUnregisteredUserIds()
    }
    val userIds = viewModel.unregisteredUserIds

    var expanded by remember { mutableStateOf(false) }
    var selectedUserId by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }
    var phoneVisible by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }
    var navigateToLogin by remember { mutableStateOf(false) }

    val passwordMismatch = confirmPassword.isNotEmpty() && password != confirmPassword
    val hasUpper = password.any { it.isUpperCase() }
    val hasLower = password.any { it.isLowerCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSymbol = password.any { !it.isLetterOrDigit() }
    val isLongEnough = password.length >= 7

    if (navigateToLogin) {
        LaunchedEffect(Unit) {
            navController.navigate(ScreenRoute.Login.route) {
                popUpTo(ScreenRoute.Register.route) { inclusive = true }
            }
            navigateToLogin = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Register", fontSize = 28.sp)
        Spacer(Modifier.height(24.dp))

        // 🔽 User ID Dropdown
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            TextField(
                value = selectedUserId,
                onValueChange = {},
                readOnly = true,
                label = { Text("My ID (Provided by your Clinician)") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                userIds.forEach { id ->
                    DropdownMenuItem(
                        text = { Text(id) },
                        onClick = {
                            selectedUserId = id
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            visualTransformation = if (phoneVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { phoneVisible = !phoneVisible }) {
                    Icon(
                        if (phoneVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmVisible = !confirmVisible }) {
                    Icon(
                        if (confirmVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = if (hasUpper) "✅ At least 1 uppercase letter" else "❌ At least 1 uppercase letter", color = if (hasUpper) Color(0xFF00C853) else Color.Red, fontSize = 14.sp)
            Text(text = if (hasLower) "✅ At least 1 lowercase letter" else "❌ At least 1 lowercase letter", color = if (hasLower) Color(0xFF00C853) else Color.Red, fontSize = 14.sp)
            Text(text = if (hasDigit) "✅ At least 1 digit" else "❌ At least 1 digit", color = if (hasDigit) Color(0xFF00C853) else Color.Red, fontSize = 14.sp)
            Text(text = if (hasSymbol) "✅ At least 1 symbol" else "❌ At least 1 symbol", color = if (hasSymbol) Color(0xFF00C853) else Color.Red, fontSize = 14.sp)
            Text(text = if (isLongEnough) "✅ At least 7 characters" else "❌ At least 7 characters", color = if (isLongEnough) Color(0xFF00C853) else Color.Red, fontSize = 14.sp)
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                when {
                    selectedUserId.isBlank() || phoneNumber.isBlank() || name.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                        errorMessage = "Please fill in all fields."
                    }
                    !isValidPassword(password) -> {
                        errorMessage = "Password must meet all conditions."
                    }
                    password != confirmPassword -> {
                        errorMessage = "Passwords do not match."
                    }
                    passwordMismatch -> { // <-- use it here
                        errorMessage = "Passwords do not match."
                    }
                    else -> {
                        viewModel.register(selectedUserId, phoneNumber, name, password) { success ->
                            if (success) {
                                viewModel.setCurrentPatient(selectedUserId)
                                sessionManager.saveSession(selectedUserId, ScreenRoute.Questionnaire.route)
                                navigateToLogin = true
                            } else {
                                errorMessage = "Invalid ID or phone number."
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text("Register", color = Color.White)
        }

        Spacer(Modifier.height(12.dp))

        TextButton(onClick = {
            navController.navigate(ScreenRoute.Login.route) {
                popUpTo(ScreenRoute.Register.route) { inclusive = true }
            }
        }) {
            Text("Already registered? Login here")
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(errorMessage, color = Color.Red)
        }
    }
}
