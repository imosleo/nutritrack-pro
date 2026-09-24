package com.fit2081.ian_34423680.nutritrackpro_app

import android.app.Application
import com.fit2081.ian_34423680.nutritrackpro_app.database.NutritrackDatabase
import com.fit2081.ian_34423680.nutritrackpro_app.repository.PatientRepository
import com.fit2081.ian_34423680.nutritrackpro_app.utils.ClinicianKeyLoader
import com.fit2081.ian_34423680.nutritrackpro_app.utils.CsvLoader
import com.fit2081.ian_34423680.nutritrackpro_app.utils.SessionManager

class NutriTrackPro : Application() {
    override fun onCreate() {
        super.onCreate()

        // Step 1: Get Room DB
        val db = NutritrackDatabase.getDatabase(this)

        // Step 2: Create SessionManager
        SessionManager.initialize(this)

        // Step 3: Create repo
        val patientRepo = PatientRepository(db.patientDao(), SessionManager)

        // Step 4: Load CSV (only if not loaded yet)
        CsvLoader.loadCsvIfFirstRun(this, patientRepo)
        ClinicianKeyLoader.loadKeyIfFirstRun(this)
    }
}
