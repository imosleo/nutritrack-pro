package com.fit2081.ian_34423680.nutritrackpro_app.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fit2081.ian_34423680.nutritrackpro_app.screen.ui.AppBottomBar
import com.fit2081.ian_34423680.nutritrackpro_app.utils.Pattern
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.ClinicianDashboardViewModel

@Composable
fun ClinicianDashboardScreen(
    navController: NavController,
    viewModel: ClinicianDashboardViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.clearInsights()
        viewModel.refreshAverages()
    }

    // Detect current nav route for tab change handling
    val navBackStackEntry by (navController as NavHostController).currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // If user navigates away (e.g. taps home, insights, coach), force exit
    LaunchedEffect(currentRoute) {
        if (currentRoute != "clinician_dashboard") {
            viewModel.clinicianLoginResult = null
        }
    }

    val insights = viewModel.generatedPatterns
    val isLoading = viewModel.isLoading

    Scaffold(
        bottomBar = {
            Column {
                // 1. Done Button
                Button(
                    onClick = {
                        viewModel.clinicianLoginResult = null // Force re-login next time
                        navController.navigate("settings") {
                            popUpTo("settings") { inclusive = false } // Optional: prevents backstack clutter
                            launchSingleTop = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8000FF)),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Done")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Done")
                }

                // 2. Bottom Navigation Bar
                AppBottomBar(navController as NavHostController)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(bottom = innerPadding.calculateBottomPadding()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(top = 30.dp, bottom = 100.dp)
        ) {
            item {
                Text("Clinician Dashboard", fontSize = 24.sp)
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = "Average HEIFA (Male) : ${viewModel.maleAverageHeifa}",
                    onValueChange = {},
                    readOnly = true
                )
                OutlinedTextField(
                    value = "Average HEIFA (Female) : ${viewModel.femaleAverageHeifa}",
                    onValueChange = {},
                    readOnly = true
                )

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = { viewModel.generateInsights(navController.context) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8000FF)),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Find")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Find Data Pattern")
                }
            }

            if (isLoading) {
                item {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 20.dp))
                }
            } else {
                items(insights.size) { index ->
                    InsightCard(insight = insights[index])
                }
            }
        }
    }
}

@Composable
fun InsightCard(insight: Pattern) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(insight.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(insight.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
