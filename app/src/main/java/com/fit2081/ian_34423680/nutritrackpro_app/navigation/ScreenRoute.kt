package com.fit2081.ian_34423680.nutritrackpro_app.navigation

sealed class ScreenRoute(val route: String) {
    object Welcome : ScreenRoute("welcome")
    object Login : ScreenRoute("login")
    object Register : ScreenRoute("register")
    object Questionnaire : ScreenRoute("questionnaire")
    object Home : ScreenRoute("home")
    object Insights : ScreenRoute("insights")
    object Settings : ScreenRoute("settings")
    object Reset : ScreenRoute("reset")
    object NutriCoach : ScreenRoute("nutricoach")
    object ClinicianDashboard : ScreenRoute("clinician_dashboard")
}