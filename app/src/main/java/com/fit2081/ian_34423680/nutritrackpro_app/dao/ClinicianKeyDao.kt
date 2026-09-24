package com.fit2081.ian_34423680.nutritrackpro_app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fit2081.ian_34423680.nutritrackpro_app.entity.ClinicianKey

@Dao
interface ClinicianKeyDao {
    @Query("SELECT * FROM clinician_key WHERE id = 0")
    suspend fun getKey(): ClinicianKey?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(key: ClinicianKey)
}