package com.fit2081.ian_34423680.nutritrackpro_app.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fit2081.ian_34423680.nutritrackpro_app.repository.PatientRepository
import com.fit2081.ian_34423680.nutritrackpro_app.utils.SessionManager
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.SettingsViewModel

class SettingsViewModelFactory(
    private val repository: PatientRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            // Creates SettingsViewModel with dependencies
            return SettingsViewModel(repository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}