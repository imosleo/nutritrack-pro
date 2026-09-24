package com.fit2081.ian_34423680.nutritrackpro_app.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "nutricoachtips")
data class NutriCoachTips(
    @PrimaryKey (autoGenerate = true)
    val tipId: Int,
    val userId: Int,
    val content: String,
)