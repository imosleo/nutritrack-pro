package com.fit2081.ian_34423680.nutritrackpro_app.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fit2081.ian_34423680.nutritrackpro_app.repository.PatientRepository
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.PatientViewModel

class PatientViewModelFactory(
    private val repository: PatientRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PatientViewModel::class.java)) {
            return PatientViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
