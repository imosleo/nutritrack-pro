package com.fit2081.ian_34423680.nutritrackpro_app.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fit2081.ian_34423680.nutritrackpro_app.repository.InsightRepository
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.InsightViewModel

class InsightViewModelFactory(private val repo: InsightRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InsightViewModel::class.java)) {
            return InsightViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
