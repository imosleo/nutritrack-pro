package com.fit2081.ian_34423680.nutritrackpro_app.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questionnaires")
data class Questionnaire(
    @PrimaryKey val userId: String,
    val foodPrefs: String?,
    val persona: String?,
    val mealTime: String?,
    val sleepTime: String?,
    val wakeTime: String?
)
