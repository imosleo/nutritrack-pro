package com.fit2081.ian_34423680.nutritrackpro_app.screen

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.fit2081.ian_34423680.nutritrackpro_app.factory.SettingsViewModelFactory
import com.fit2081.ian_34423680.nutritrackpro_app.repository.PatientRepository
import com.fit2081.ian_34423680.nutritrackpro_app.screen.ui.AppBottomBar
import com.fit2081.ian_34423680.nutritrackpro_app.utils.SecurityUtils
import com.fit2081.ian_34423680.nutritrackpro_app.utils.SessionManager
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.ClinicianDashboardViewModel
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.PatientViewModel
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    repository: PatientRepository,
    sessionManager: SessionManager,
    navController: NavHostController,
    patientViewModel: PatientViewModel,
    clinicianDashboardViewModel: ClinicianDashboardViewModel
) {
    val viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(repository, sessionManager)
    )
    val patient by viewModel.currentPatient.collectAsState()
    val context = LocalContext.current

    var showNameDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showClinicianDialog by remember { mutableStateOf(false) }
    var clinicianKeyInput by remember { mutableStateOf("") }

    val loginResult = clinicianDashboardViewModel.clinicianLoginResult

    LaunchedEffect(loginResult) {
        if (loginResult == true) {
            clinicianKeyInput = ""
            showClinicianDialog = false
            navController.navigate("clinician_dashboard")
        }
    }

    Scaffold(
        bottomBar = { AppBottomBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Settings", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(24.dp))

                Text("ACCOUNT", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))

                patient?.let {
                    SettingsRow(Icons.Default.Person, it.name ?: "Unregistered")
                    SettingsRow(Icons.Default.Phone, it.phoneNumber)
                    SettingsRow(Icons.Default.Badge, it.userId.toString())
                }

                Spacer(modifier = Modifier.height(24.dp))
                Divider()
                Spacer(modifier = Modifier.height(24.dp))

                Text("OTHER SETTINGS", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))

                SettingActionRow(Icons.Default.Edit, "Change Name") {
                    showNameDialog = true
                }
                SettingActionRow(Icons.Default.Lock, "Change Password") {
                    showPasswordDialog = true
                }
                SettingActionRow(Icons.Default.Logout, "Logout") {
                    viewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
                SettingActionRow(Icons.Default.MedicalServices, "Clinician Login") {
                    showClinicianDialog = true
                }
            }
        }
    }
// Change Name Dialog
    if (showNameDialog) {
        var newName by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showNameDialog = false },
            title = { Text("Change Name") },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Enter new name") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newName.isBlank()) {
                        Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.updateName(newName) { success ->
                            Toast.makeText(
                                context,
                                if (success) "Name updated" else "Failed to update name",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        showNameDialog = false
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNameDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
// Change Password Dialog
    if (showPasswordDialog) {
        var oldPassword by remember { mutableStateOf("") }
        var newPassword by remember { mutableStateOf("") }
        var confirmNewPassword by remember { mutableStateOf("") }
        val currentPassword = patient?.password ?: ""

        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("Change Password") },
            text = {
                Column {
                    OutlinedTextField(
                        value = oldPassword,
                        onValueChange = { oldPassword = it },
                        label = { Text("Old Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = confirmNewPassword,
                        onValueChange = { confirmNewPassword = it },
                        label = { Text("Confirm New Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    when {
                        !SecurityUtils.verifyPassword(oldPassword, currentPassword) -> {
                            Toast.makeText(context, "Old password is incorrect", Toast.LENGTH_SHORT).show()
                        }
                        newPassword.isBlank() || confirmNewPassword.isBlank() -> {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        }
                        newPassword != confirmNewPassword -> {
                            Toast.makeText(context, "New passwords do not match", Toast.LENGTH_SHORT).show()
                        }
                        SecurityUtils.verifyPassword(newPassword, currentPassword) -> {
                            Toast.makeText(context, "New password must be different from old", Toast.LENGTH_SHORT).show()
                        }
                        !(newPassword.any { it.isUpperCase() } &&
                                newPassword.any { it.isLowerCase() } &&
                                newPassword.any { it.isDigit() } &&
                                newPassword.any { !it.isLetterOrDigit() } &&
                                newPassword.length >= 7) -> {
                            Toast.makeText(context, "Password must contain:", Toast.LENGTH_SHORT).show()
                            Toast.makeText(context, "- Uppercase & lowercase letters", Toast.LENGTH_SHORT).show()
                            Toast.makeText(context, "- A digit, a symbol, and be ≥ 7 characters", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            val hashedPassword = SecurityUtils.hashPassword(newPassword)
                            viewModel.updatePassword(hashedPassword) { success ->
                                Toast.makeText(
                                    context,
                                    if (success) "Password updated" else "Failed to update password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            showPasswordDialog = false
                        }
                    }
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showPasswordDialog = false }) { Text("Cancel") }
            }
        )
    }


    // Clinician Login Dialog (Updated with hashing check)
    if (showClinicianDialog) {
        AlertDialog(
            onDismissRequest = {
                showClinicianDialog = false
                clinicianKeyInput = ""
            },
            title = { Text("Clinician Key") },
            text = {
                OutlinedTextField(
                    value = clinicianKeyInput,
                    onValueChange = { clinicianKeyInput = it },
                    label = { Text("Enter your clinician key") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    clinicianDashboardViewModel.loginWithClinicianKey(clinicianKeyInput)
                }) {
                    Text("Login")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showClinicianDialog = false
                    clinicianKeyInput = ""
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SettingsRow(icon: ImageVector, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(12.dp))
        Text(value)
    }
}

@Composable
fun SettingActionRow(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text(label)
        }
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
    }
}
