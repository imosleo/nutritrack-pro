package com.fit2081.ian_34423680.nutritrackpro_app.dao

import androidx.room.*
import com.fit2081.ian_34423680.nutritrackpro_app.entity.Patient

@Dao
interface PatientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(patient: Patient)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(patients: List<Patient>)

    @Query("SELECT * FROM patients")
    suspend fun getAllPatients(): List<Patient>

    @Query("SELECT * FROM patients WHERE userId = :id LIMIT 1")
    suspend fun getById(id: Int): Patient?

    @Query("SELECT userId FROM patients")
    suspend fun getAllUserIds(): List<Int>

    @Query("SELECT * FROM patients WHERE userId = :userId LIMIT 1")
    suspend fun getPatientById(userId: Int): Patient?

    @Query("UPDATE patients SET password = :newPassword WHERE userId = :userId")
    suspend fun updatePassword(userId: Int, newPassword: String)

    @Query("UPDATE patients SET name = :newName WHERE userId = :userId")
    suspend fun updateName(userId: Int, newName: String)

    @Query("SELECT * FROM patients WHERE password IS NOT NULL")
    suspend fun getRegisteredPatients(): List<Patient>

    @Query("SELECT * FROM patients WHERE password IS NULL")
    suspend fun getUnregisteredPatients(): List<Patient>

}