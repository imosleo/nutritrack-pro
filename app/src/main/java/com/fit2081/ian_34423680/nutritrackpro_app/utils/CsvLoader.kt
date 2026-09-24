package com.fit2081.ian_34423680.nutritrackpro_app.utils

import android.content.Context
import android.content.SharedPreferences
import com.fit2081.ian_34423680.nutritrackpro_app.entity.Patient
import com.fit2081.ian_34423680.nutritrackpro_app.repository.PatientRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object CsvLoader {
    private const val PREF_NAME = "nutritrackpro_prefs"
    private const val KEY_CSV_LOADED = "csv_loaded"

    fun loadCsvIfFirstRun(context: Context, patientRepo: PatientRepository) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        if (!prefs.getBoolean(KEY_CSV_LOADED, false)) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val csvData = readCsv(context)
                    val patients = getPatientData(csvData)
                    patients.forEach { patientRepo.insert(it) }
                    prefs.edit().putBoolean(KEY_CSV_LOADED, true).apply()
                } catch (_: Exception) { }
            }
        }
    }

    fun readCsv(context: Context): List<Array<String>> {
        val csvData = mutableListOf<Array<String>>()
        try {
            val inputStream = context.assets.open("nutritrackpro_app.csv")
            inputStream.bufferedReader().useLines { lines ->
                lines.forEach { line ->
                    val tokens = line.split(",").map { it.trim() }.toTypedArray()
                    csvData.add(tokens)
                }
            }
        } catch (_: Exception) { }
        return csvData
    }

    fun getPatientData(csvData: List<Array<String>>): List<Patient> {
        if (csvData.isEmpty()) return emptyList()

        val headers = csvData.first()
        val patients = mutableListOf<Patient>()

        for (i in 1 until csvData.size) {
            val row = csvData[i]
            try {
                if (row.size < 3) continue
                val userId = row[1].toIntOrNull() ?: continue
                val gender = row[2]
                val phoneNumber = row.getOrNull(0) ?: ""
                val isFemale = gender.equals("Female", ignoreCase = true)

                fun getScoreValue(baseColumnName: String): Double {
                    val columnName = baseColumnName + if (isFemale) "Female" else "Male"
                    val columnIndex = headers.indexOfFirst { it == columnName }
                    return if (columnIndex >= 0 && columnIndex < row.size) {
                        row[columnIndex].toDoubleOrNull() ?: 0.0
                    } else 0.0
                }

                fun getScoreValueWithoutGender(baseColumnName: String): Double {
                    val columnIndex = headers.indexOfFirst { it == baseColumnName }
                    return if (columnIndex >= 0 && columnIndex < row.size) {
                        row[columnIndex].toDoubleOrNull() ?: 0.0
                    } else 0.0
                }

                val patient = Patient(
                    userId = userId,
                    sex = gender,
                    phoneNumber = phoneNumber,
                    name = "",
                    password = "",
                    VegetablesHEIFAscore = getScoreValue("VegetablesHEIFAscore"),
                    FruitHEIFAscore = getScoreValue("FruitHEIFAscore"),
                    GrainsandcerealsHEIFAscore = getScoreValue("GrainsandcerealsHEIFAscore"),
                    WholegrainsHEIFAscore = getScoreValue("WholegrainsHEIFAscore"),
                    MeatandalternativesHEIFAscore = getScoreValue("MeatandalternativesHEIFAscore"),
                    DairyandalternativesHEIFAscore = getScoreValue("DairyandalternativesHEIFAscore"),
                    WaterHEIFAscore = getScoreValue("WaterHEIFAscore"),
                    UnsaturatedFatHEIFAscore = getScoreValue("UnsaturatedFatHEIFAscore"),
                    SaturatedFatHEIFAscore = getScoreValue("SaturatedFatHEIFAscore"),
                    SodiumHEIFAscore = getScoreValue("SodiumHEIFAscore"),
                    SugarHEIFAscore = getScoreValue("SugarHEIFAscore"),
                    AlcoholHEIFAscore = getScoreValue("AlcoholHEIFAscore"),
                    DiscretionaryHEIFAscore = getScoreValue("DiscretionaryHEIFAscore"),
                    HEIFAtotalscore = getScoreValue("HEIFAtotalscore"),
                    FruitServingScore = getScoreValueWithoutGender("Fruitservesize"),
                    FruitVariationsScore = getScoreValueWithoutGender("Fruitvariationsscore")
                )

                patients.add(patient)
            } catch (_: Exception) { }
        }

        return patients
    }
}
