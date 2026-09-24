package com.fit2081.ian_34423680.nutritrackpro_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fit2081.ian_34423680.nutritrackpro_app.screen.NutriCoachScreen
import com.fit2081.ian_34423680.nutritrackpro_app.screen.ResetDetailsScreen
import com.fit2081.ian_34423680.nutritrackpro_app.database.NutritrackDatabase
import com.fit2081.ian_34423680.nutritrackpro_app.factory.ClinicianDashboardViewModelFactory
import com.fit2081.ian_34423680.nutritrackpro_app.factory.HomeViewModelFactory
import com.fit2081.ian_34423680.nutritrackpro_app.factory.PatientViewModelFactory
import com.fit2081.ian_34423680.nutritrackpro_app.factory.QuestionnaireFactory
import com.fit2081.ian_34423680.nutritrackpro_app.factory.InsightViewModelFactory
import com.fit2081.ian_34423680.nutritrackpro_app.factory.LoginViewModelFactory
import com.fit2081.ian_34423680.nutritrackpro_app.factory.NutriCoachViewModelFactory
import com.fit2081.ian_34423680.nutritrackpro_app.repository.ClinicianKeyRepository
import com.fit2081.ian_34423680.nutritrackpro_app.repository.FoodIntakeRepository
import com.fit2081.ian_34423680.nutritrackpro_app.repository.PatientRepository
import com.fit2081.ian_34423680.nutritrackpro_app.repository.InsightRepository
import com.fit2081.ian_34423680.nutritrackpro_app.repository.NutriCoachTipsRepository
import com.fit2081.ian_34423680.nutritrackpro_app.screen.*
import com.fit2081.ian_34423680.nutritrackpro_app.utils.SessionManager
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.*

@Composable
fun NavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val db = remember { NutritrackDatabase.getDatabase(context) }

    // Use SessionManager object directly
    val patientRepo = remember { PatientRepository(db.patientDao(), SessionManager) }
    val questionnaireRepo = remember { FoodIntakeRepository(db.questionnaireDao()) }
    val insightRepo = remember { InsightRepository(db.patientDao()) }
    val nutriCoachRepo = remember { NutriCoachTipsRepository(context) }


    val patientViewModel: PatientViewModel = viewModel(
        factory = PatientViewModelFactory(patientRepo)
    )

    val questionnaireViewModel: QuestionnaireViewModel = viewModel(
        factory = QuestionnaireFactory(questionnaireRepo)
    )

    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(patientViewModel)
    )

    val insightViewModel: InsightViewModel = viewModel(
        factory = InsightViewModelFactory(insightRepo)
    )

    val currentUserId = patientViewModel.currentPatient?.userId?.toString()

    val nutriCoachViewModel: NutriCoachViewModel = viewModel(
        factory = NutriCoachViewModelFactory(
            patientRepo,
            nutriCoachRepo
        )
    )

    val loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(patientRepo, SessionManager)
    )

    val clinicianDashboardViewModel: ClinicianDashboardViewModel = viewModel(
        factory = ClinicianDashboardViewModelFactory(
            patientRepo,
            ClinicianKeyRepository(db.clinicianKeyDao())
        )
    )

    NavHost(
        navController = navController,
        startDestination = ScreenRoute.Welcome.route
    ) {
        composable(ScreenRoute.Welcome.route) {
            WelcomeScreen(
                navController = navController,
                patientViewModel = patientViewModel,
                questionnaireViewModel = questionnaireViewModel
            )
        }

        composable(ScreenRoute.Login.route) {
            LoginScreen(
                navController = navController,
                viewModel = loginViewModel,
                sessionManager = SessionManager,
                patientViewModel = patientViewModel,
                questionnaireViewModel = questionnaireViewModel
            )
        }

        composable(ScreenRoute.Register.route) {
            RegisterScreen(
                navController,
                viewModel = patientViewModel
            )
        }

        composable(ScreenRoute.Reset.route) {
            ResetDetailsScreen(
                navController = navController,
                repository = patientRepo
            )
        }

        composable(ScreenRoute.Questionnaire.route) {
            QuestionnaireScreen(
                navController = navController,
                viewModel = questionnaireViewModel,
                sessionManager = SessionManager,
                userId = patientViewModel.currentPatient?.userId?.toString().orEmpty()
            )
        }

        composable(ScreenRoute.Home.route) {
            HomeScreen(
                navController = navController,
                viewModel = homeViewModel,
                patientViewModel = patientViewModel,
                sessionManager = SessionManager
            )
        }

        composable(ScreenRoute.Insights.route) {
            InsightsScreen(
                navController = navController,
                patientViewModel = patientViewModel,
                insightViewModel = insightViewModel
            )
        }
        composable(ScreenRoute.Settings.route) {
            SettingsScreen(
                repository = patientRepo,
                sessionManager = SessionManager,
                navController = navController,
                patientViewModel = patientViewModel,
                clinicianDashboardViewModel = clinicianDashboardViewModel
            )
        }
        composable(ScreenRoute.NutriCoach.route) {
            NutriCoachScreen(
                navController = navController,
                viewModel = nutriCoachViewModel
            )
        }
        composable(ScreenRoute.ClinicianDashboard.route) {
            ClinicianDashboardScreen(
                navController = navController,
                viewModel = clinicianDashboardViewModel
            )
        }
    }
}
