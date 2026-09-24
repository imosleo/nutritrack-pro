@file:OptIn(ExperimentalMaterial3Api::class)

package com.fit2081.ian_34423680.nutritrackpro_app.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.fit2081.ian_34423680.nutritrackpro_app.factory.ResetDetailsViewModelFactory
import com.fit2081.ian_34423680.nutritrackpro_app.repository.PatientRepository
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.ResetDetailsViewModel

@Composable
fun ResetDetailsScreen(
    navController: NavHostController,
    repository: PatientRepository
) {
    val viewModel: ResetDetailsViewModel = viewModel(
        factory = ResetDetailsViewModelFactory(repository)
    )

    val userIds by viewModel.allUserIds.collectAsState()
    val currentPatient by viewModel.currentPatient.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()

    var selectedId by remember { mutableStateOf("") }
    var expandedId by remember { mutableStateOf(false) }

    var resetOption by remember { mutableStateOf("Name") }
    val options = listOf("Name", "Password")
    var expandedOption by remember { mutableStateOf(false) }

    var newValue by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("") }
    var phoneVisible by remember { mutableStateOf(false) }

    val hasUpper = newValue.any { it.isUpperCase() }
    val hasLower = newValue.any { it.isLowerCase() }
    val hasDigit = newValue.any { it.isDigit() }
    val hasSymbol = newValue.any { !it.isLetterOrDigit() }
    val isLongEnough = newValue.length >= 7

    // Fetch current patient info when selected
    LaunchedEffect(selectedId) {
        if (selectedId.isNotBlank()) {
            viewModel.fetchPatientById(selectedId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.Start)) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text("Reset Details", fontSize = 24.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(24.dp))

        // User ID selection
        ExposedDropdownMenuBox(expanded = expandedId, onExpandedChange = { expandedId = !expandedId }) {
            TextField(
                value = selectedId,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select User ID") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedId) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedId,
                onDismissRequest = { expandedId = false }
            ) {
                userIds.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            selectedId = it
                            expandedId = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Phone number verification
        TextField(
            value = phoneInput,
            onValueChange = { phoneInput = it },
            label = { Text("Phone Number (for verification)") },
            visualTransformation = if (phoneVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { phoneVisible = !phoneVisible }) {
                    Icon(
                        imageVector = if (phoneVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (phoneVisible) "Hide phone number" else "Show phone number"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Field to reset (Name or Password)
        ExposedDropdownMenuBox(expanded = expandedOption, onExpandedChange = { expandedOption = !expandedOption }) {
            TextField(
                value = resetOption,
                onValueChange = {},
                readOnly = true,
                label = { Text("Update Field") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedOption) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedOption,
                onDismissRequest = { expandedOption = false }
            ) {
                options.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            resetOption = it
                            expandedOption = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // New value input
        TextField(
            value = newValue,
            onValueChange = { newValue = it },
            label = { Text("New $resetOption") },
            modifier = Modifier.fillMaxWidth()
        )

        // Password requirements checklist
        if (resetOption == "Password") {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (hasUpper) "✅ At least 1 uppercase letter" else "❌ At least 1 uppercase letter",
                    color = if (hasUpper) Color(0xFF00C853) else Color.Red,
                    fontSize = 14.sp
                )
                Text(
                    text = if (hasLower) "✅ At least 1 lowercase letter" else "❌ At least 1 lowercase letter",
                    color = if (hasLower) Color(0xFF00C853) else Color.Red,
                    fontSize = 14.sp
                )
                Text(
                    text = if (hasDigit) "✅ At least 1 digit" else "❌ At least 1 digit",
                    color = if (hasDigit) Color(0xFF00C853) else Color.Red,
                    fontSize = 14.sp
                )
                Text(
                    text = if (hasSymbol) "✅ At least 1 symbol" else "❌ At least 1 symbol",
                    color = if (hasSymbol) Color(0xFF00C853) else Color.Red,
                    fontSize = 14.sp
                )
                Text(
                    text = if (isLongEnough) "✅ At least 7 characters" else "❌ At least 7 characters",
                    color = if (isLongEnough) Color(0xFF00C853) else Color.Red,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Update button
        Button(
            onClick = {
                viewModel.resetField(
                    userId = selectedId,
                    phone = phoneInput,
                    field = resetOption,
                    newValue = newValue
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text("Update", color = Color.White)
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(errorMessage, color = Color.Red)
        }

        if (successMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(successMessage, color = Color(0xFF00C853))
        }
    }
}