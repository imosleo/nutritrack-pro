package com.fit2081.ian_34423680.nutritrackpro_app.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class Patient(
    @PrimaryKey
    val userId: Int,
    val sex: String,
    val phoneNumber: String,
    val name: String = "",           // null if not yet registered
    val password: String? = "",

    // CSV Data
    val VegetablesHEIFAscore: Double,
    val FruitHEIFAscore: Double,
    val GrainsandcerealsHEIFAscore: Double,
    val WholegrainsHEIFAscore: Double,
    val MeatandalternativesHEIFAscore: Double,
    val DairyandalternativesHEIFAscore: Double,
    val WaterHEIFAscore: Double,
    val UnsaturatedFatHEIFAscore: Double,
    val SaturatedFatHEIFAscore: Double,
    val SodiumHEIFAscore: Double,
    val SugarHEIFAscore: Double,
    val AlcoholHEIFAscore: Double,
    val DiscretionaryHEIFAscore: Double,
    val HEIFAtotalscore: Double,
    val FruitServingScore: Double,
    val FruitVariationsScore: Double,
)
