package com.fit2081.ian_34423680.nutritrackpro_app.dao

import androidx.room.*
import com.fit2081.ian_34423680.nutritrackpro_app.entity.Questionnaire

@Dao
interface QuestionnaireDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(questionnaire: Questionnaire)

    @Update
    suspend fun update(questionnaire: Questionnaire)

    @Delete
    suspend fun delete(questionnaire: Questionnaire)

    @Query("SELECT * FROM questionnaires WHERE userId = :userId LIMIT 1")
    suspend fun getByUserId(userId: String): Questionnaire?

    @Query("SELECT * FROM questionnaires")
    suspend fun getAll(): List<Questionnaire>

    @Query("SELECT * FROM questionnaires WHERE userId = :userId LIMIT 1")
    suspend fun getQuestionnaireByUserId(userId: String): Questionnaire?

}