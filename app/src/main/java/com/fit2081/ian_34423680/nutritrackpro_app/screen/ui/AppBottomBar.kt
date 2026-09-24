package com.fit2081.ian_34423680.nutritrackpro_app.screen.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fit2081.ian_34423680.nutritrackpro_app.navigation.ScreenRoute
import com.fit2081.ian_34423680.nutritrackpro_app.utils.SessionManager

@Composable
fun AppBottomBar(navController: NavHostController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { if (currentRoute != "home") SessionManager.setLastRoute(ScreenRoute.Home.route)
                navController.navigate(ScreenRoute.Home.route) { launchSingleTop = true } },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = currentRoute == "insights",
            onClick = { if (currentRoute != "insights") navController.navigate("insights") { launchSingleTop = true } },
            icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
            label = { Text("Insights") }
        )
        NavigationBarItem(
            selected = currentRoute == "nutricoach",
            onClick = { if (currentRoute != "nutricoach") navController.navigate("nutricoach") { launchSingleTop = true } },
            icon = { Icon(Icons.Default.FitnessCenter, contentDescription = null) },
            label = { Text("NutriCoach") }
        )
        NavigationBarItem(
            selected = currentRoute == "settings",
            onClick = { if (currentRoute != "settings") navController.navigate("settings") { launchSingleTop = true } },
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { Text("Settings") }
        )
    }
}