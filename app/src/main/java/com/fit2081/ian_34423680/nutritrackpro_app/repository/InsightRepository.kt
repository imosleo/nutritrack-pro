package com.fit2081.ian_34423680.nutritrackpro_app.repository

import com.fit2081.ian_34423680.nutritrackpro_app.dao.PatientDao
import com.fit2081.ian_34423680.nutritrackpro_app.entity.Patient

class InsightRepository(private val patientDao: PatientDao) {

    suspend fun getPatientById(userId: String): Patient? {
        return patientDao.getPatientById(userId.toInt())
    }
}
