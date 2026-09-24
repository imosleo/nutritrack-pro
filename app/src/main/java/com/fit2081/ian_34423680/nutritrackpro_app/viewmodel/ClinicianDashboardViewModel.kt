package com.fit2081.ian_34423680.nutritrackpro_app.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.ian_34423680.nutritrackpro_app.BuildConfig
import com.fit2081.ian_34423680.nutritrackpro_app.repository.ClinicianKeyRepository
import com.fit2081.ian_34423680.nutritrackpro_app.repository.PatientRepository
import com.fit2081.ian_34423680.nutritrackpro_app.utils.Pattern
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import kotlin.math.roundToInt

class ClinicianDashboardViewModel(
    private val patientRepository: PatientRepository,
    private val clinicianKeyRepository: ClinicianKeyRepository
) : ViewModel() {

    var maleAverageHeifa by mutableStateOf(0.0)
        private set

    var femaleAverageHeifa by mutableStateOf(0.0)
        private set

    var healthyFoodAverageScore by mutableStateOf(0.0)
        private set

    var fatAverageScore by mutableStateOf(0.0)
        private set

    var alcoholAverageScore by mutableStateOf(0.0)
        private set

    var waterAverageScore by mutableStateOf(0.0)
        private set

    var generatedPatterns by mutableStateOf(emptyList<Pattern>())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var clinicianLoginResult by mutableStateOf<Boolean?>(null)

    fun clearInsights() {
        generatedPatterns = emptyList()
    }

    fun refreshAverages() {
        calcAverages()
    }

    private fun calcAverages() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val patients = patientRepository.getAllPatients()

                val males = patients.filter { it.sex.equals("male", ignoreCase = true) }
                val females = patients.filter { it.sex.equals("female", ignoreCase = true) }

                maleAverageHeifa = roundTo1Decimal(males.map { it.HEIFAtotalscore }.average())
                femaleAverageHeifa = roundTo1Decimal(females.map { it.HEIFAtotalscore }.average())

                healthyFoodAverageScore = roundTo1Decimal(
                    patients.map { it.VegetablesHEIFAscore + it.FruitHEIFAscore }.average()
                )

                fatAverageScore = roundTo1Decimal(
                    patients.map { it.HEIFAtotalscore }.average()
                )

                alcoholAverageScore = roundTo1Decimal(
                    patients.map { it.AlcoholHEIFAscore }.average()
                )

                waterAverageScore = roundTo1Decimal(
                    patients.map { it.WaterHEIFAscore }.average()
                )
            } catch (_: Exception) {
            }
        }
    }

    private fun roundTo1Decimal(value: Double): Double {
        return (value * 10).roundToInt() / 10.0
    }

    fun loginWithClinicianKey(inputKey: String) {
        viewModelScope.launch {
            val result = clinicianKeyRepository.verifyKey(inputKey)
            clinicianLoginResult = result
        }
    }

    fun generateInsights(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading = true

                val model = GenerativeModel(
                    modelName = "gemini-1.5-flash",
                    apiKey = BuildConfig.apiKey
                )

                val prompt =
                    """
                    You are an energetic anime doctor 🌟 helping review health stats of your patients in a futuristic wellness clinic!
                    
                    Today's collected average stats are:
                    - 💙 Male HEIFA Score: $maleAverageHeifa
                    - 💖 Female HEIFA Score: $femaleAverageHeifa
                    - 🥦 Healthy Food Score: $healthyFoodAverageScore
                    - 🍔 Fat Score: $fatAverageScore (Max: 5)
                    - 🍷 Alcohol Score: $alcoholAverageScore
                    - 💧 Water Score: $waterAverageScore (Max: 5)
                    
                    Based on this data, please provide 3 kawaii-style clinical insights in JSON format!
                    
                    The output must be:
                    [
                        {"tipId": 1, "title": "Insight Title", "description": "Cheerful yet helpful health insight with emojis"},
                        {"tipId": 2, "title": "...", "description": "..."},
                        {"tipId": 3, "title": "...", "description": "..."}
                    ]
                    
                    Be warm, optimistic, and use emojis to motivate healthy habits — just like a doctor in an anime healing hearts and bellies! 🍱🩺✨
                    """.trimIndent()

                val result = model.generateContent(prompt)
                val text = result.text ?: return@launch

                val cleanedText = text
                    .replace("```json", "")
                    .replace("```", "")
                    .trim()

                try {
                    val jsonArray = JSONArray(cleanedText)
                    val newPatterns = mutableListOf<Pattern>()

                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val tipId = obj.optInt("tipId", i + 1)
                        val title = obj.optString("title")
                        val desc = obj.optString("description")
                        if (title.isNotBlank() && desc.isNotBlank()) {
                            newPatterns.add(Pattern(tipId, title, desc))
                        }
                    }

                    generatedPatterns = newPatterns
                } catch (_: Exception) { }

            } catch (_: Exception) {
            } finally {
                isLoading = false
            }
        }
    }
}
