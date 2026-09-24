package com.fit2081.ian_34423680.nutritrackpro_app.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fit2081.ian_34423680.nutritrackpro_app.repository.ClinicianKeyRepository
import com.fit2081.ian_34423680.nutritrackpro_app.repository.PatientRepository
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.ClinicianDashboardViewModel

class ClinicianDashboardViewModelFactory(
    private val patientRepository: PatientRepository,
    private val clinicianKeyRepository: ClinicianKeyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClinicianDashboardViewModel::class.java)) {
            return ClinicianDashboardViewModel(patientRepository, clinicianKeyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
