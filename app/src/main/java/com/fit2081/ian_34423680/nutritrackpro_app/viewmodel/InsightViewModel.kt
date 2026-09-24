package com.fit2081.ian_34423680.nutritrackpro_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.ian_34423680.nutritrackpro_app.entity.Patient
import com.fit2081.ian_34423680.nutritrackpro_app.repository.InsightRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InsightViewModel(private val repo: InsightRepository) : ViewModel() {

    private val _heifaScores = MutableStateFlow<Map<String, Float>>(emptyMap())
    val heifaScores: StateFlow<Map<String, Float>> = _heifaScores

    private val _totalScore = MutableStateFlow<Float?>(null)
    val totalScore: StateFlow<Float?> = _totalScore

    fun loadFromPatient(patient: Patient?) {
        if (patient == null) return

        _heifaScores.value = mapOf(
            "Vegetables" to patient.VegetablesHEIFAscore.toFloat(),
            "Fruits" to patient.FruitHEIFAscore.toFloat(),
            "Grains and Cereals" to patient.GrainsandcerealsHEIFAscore.toFloat(),
            "Whole Grains" to patient.WholegrainsHEIFAscore.toFloat(),
            "Meat and Alternatives" to patient.MeatandalternativesHEIFAscore.toFloat(),
            "Dairy and Alternatives" to patient.DairyandalternativesHEIFAscore.toFloat(),
            "Sodium" to patient.SodiumHEIFAscore.toFloat(),
            "Alcohol" to patient.AlcoholHEIFAscore.toFloat(),
            "Water" to patient.WaterHEIFAscore.toFloat(),
            "Sugar" to patient.SugarHEIFAscore.toFloat(),
            "Saturated Fats" to patient.SaturatedFatHEIFAscore.toFloat(),
            "Unsaturated Fats" to patient.UnsaturatedFatHEIFAscore.toFloat()
        )

        _totalScore.value = patient.HEIFAtotalscore.toFloat()
    }

    fun loadByUserId(userId: String) {
        viewModelScope.launch {
            val patient = repo.getPatientById(userId)
            loadFromPatient(patient)
        }
    }
}

