package com.fit2081.ian_34423680.nutritrackpro_app.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clinician_key")
data class ClinicianKey(
    @PrimaryKey val id: Int = 0,
    val key: String
)