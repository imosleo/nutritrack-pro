package com.fit2081.ian_34423680.nutritrackpro_app.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fit2081.ian_34423680.nutritrackpro_app.repository.NutriCoachTipsRepository
import com.fit2081.ian_34423680.nutritrackpro_app.repository.PatientRepository
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.NutriCoachViewModel

class NutriCoachViewModelFactory(
    private val patientRepo: PatientRepository,
    private val tipRepo: NutriCoachTipsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NutriCoachViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NutriCoachViewModel(patientRepo, tipRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
