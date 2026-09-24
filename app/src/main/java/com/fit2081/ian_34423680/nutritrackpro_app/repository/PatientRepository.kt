package com.fit2081.ian_34423680.nutritrackpro_app.repository

import com.fit2081.ian_34423680.nutritrackpro_app.dao.PatientDao
import com.fit2081.ian_34423680.nutritrackpro_app.entity.Patient
import com.fit2081.ian_34423680.nutritrackpro_app.utils.SecurityUtils
import com.fit2081.ian_34423680.nutritrackpro_app.utils.SessionManager

class PatientRepository(
    private val dao: PatientDao,
    private val sessionManager: SessionManager
) {

    suspend fun insert(patient: Patient) {
        dao.insert(patient)
    }

    suspend fun insertAll(patients: List<Patient>) {
        dao.insertAll(patients)
    }

    suspend fun getById(userId: Int): Patient? {
        return dao.getById(userId)
    }

    suspend fun getAllUserIds(): List<Int> {
        return dao.getAllUserIds()
    }

    suspend fun getAllPatients(): List<Patient> {
        return dao.getAllPatients()
    }

    suspend fun validateUser(userId: Int, password: String): Boolean {
        val patient = dao.getById(userId)
        return patient?.password?.let { storedHash ->
            SecurityUtils.verifyPassword(password, storedHash)
        } ?: false
    }

    suspend fun getPatientById(userId: Int): Patient? {
        return dao.getById(userId)
    }

    suspend fun updatePassword(userId: Int, newPassword: String) {
        dao.updatePassword(userId, newPassword)
    }

    suspend fun updateName(userId: Int, newName: String) {
        dao.updateName(userId, newName)
    }
}