package com.fit2081.ian_34423680.nutritrackpro_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.fit2081.ian_34423680.nutritrackpro_app.navigation.NavGraph
import com.fit2081.ian_34423680.nutritrackpro_app.ui.theme.A3nutritrackTheme
import com.fit2081.ian_34423680.nutritrackpro_app.utils.SessionManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            SessionManager.initialize(applicationContext)

            A3nutritrackTheme {
                val navController = rememberNavController()
                NavGraph(navController)
            }
        }
    }
}
