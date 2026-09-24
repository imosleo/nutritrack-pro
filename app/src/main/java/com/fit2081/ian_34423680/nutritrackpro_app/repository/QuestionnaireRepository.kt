package com.fit2081.ian_34423680.nutritrackpro_app.repository

import com.fit2081.ian_34423680.nutritrackpro_app.dao.QuestionnaireDao
import com.fit2081.ian_34423680.nutritrackpro_app.entity.Questionnaire

class FoodIntakeRepository(private val dao: QuestionnaireDao) {

    suspend fun insert(questionnaire: Questionnaire) {
        dao.insert(questionnaire)
    }

    suspend fun update(questionnaire: Questionnaire) {
        dao.update(questionnaire)
    }

    suspend fun delete(questionnaire: Questionnaire) {
        dao.delete(questionnaire)
    }

    suspend fun getByUserId(userId: String): Questionnaire? {
        return dao.getByUserId(userId)
    }

    suspend fun getQuestionnaireByUserId(userId: String): Questionnaire? {
        return dao.getQuestionnaireByUserId(userId)
    }

    suspend fun getAll(): List<Questionnaire> {
        return dao.getAll()
    }
}
