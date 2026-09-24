package com.fit2081.ian_34423680.nutritrackpro_app.repository

import android.content.Context
import com.fit2081.ian_34423680.nutritrackpro_app.dao.NutriCoachTipsDao
import com.fit2081.ian_34423680.nutritrackpro_app.database.NutritrackDatabase
import com.fit2081.ian_34423680.nutritrackpro_app.entity.NutriCoachTips
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NutriCoachTipsRepository {

    private val nutriCoachTipsDao: NutriCoachTipsDao

    constructor(context: Context) {
        nutriCoachTipsDao = NutritrackDatabase.getDatabase(context).nutriCoachDao()
    }

    suspend fun insert(tip: NutriCoachTips) = withContext(Dispatchers.IO) {
        nutriCoachTipsDao.insert(tip)
    }

    suspend fun update(tip: NutriCoachTips) = withContext(Dispatchers.IO) {
        nutriCoachTipsDao.update(tip)
    }

    suspend fun getTipsByUserId(userId: Int): List<NutriCoachTips> = withContext(Dispatchers.IO) {
        nutriCoachTipsDao.getTipsByUserId(userId)
    }
}
