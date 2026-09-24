package com.fit2081.ian_34423680.nutritrackpro_app.repository

import com.fit2081.ian_34423680.nutritrackpro_app.dao.ClinicianKeyDao
import com.fit2081.ian_34423680.nutritrackpro_app.entity.ClinicianKey
import com.fit2081.ian_34423680.nutritrackpro_app.utils.SecurityUtils

class ClinicianKeyRepository(private val dao: ClinicianKeyDao) {

    suspend fun getKey(): ClinicianKey? { //future-proofing for potential nullability
        return dao.getKey()
    }

    suspend fun insertKey(key: ClinicianKey) { //future-proofing for potential nullability
        dao.insertKey(key)
    }

    suspend fun verifyKey(input: String): Boolean {
        val stored = dao.getKey()?.key ?: return false
        return SecurityUtils.verifyPassword(input, stored)
    }
}