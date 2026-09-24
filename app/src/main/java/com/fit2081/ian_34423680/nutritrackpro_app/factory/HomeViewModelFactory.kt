package com.fit2081.ian_34423680.nutritrackpro_app.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.HomeViewModel
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.PatientViewModel

class HomeViewModelFactory(
    private val patientViewModel: PatientViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(patientViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
