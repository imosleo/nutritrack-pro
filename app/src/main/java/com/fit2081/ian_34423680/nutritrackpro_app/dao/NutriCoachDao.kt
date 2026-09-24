package com.fit2081.ian_34423680.nutritrackpro_app.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.fit2081.ian_34423680.nutritrackpro_app.entity.NutriCoachTips

@Dao
interface NutriCoachTipsDao {
    @Insert
    suspend fun insert(nutriCoachTips: NutriCoachTips)

    @Update
    suspend fun update(nutriCoachTips: NutriCoachTips)

    @Delete
    suspend fun delete(nutriCoachTips: NutriCoachTips)

    @Query("DELETE FROM nutricoachtips")
    suspend fun deleteAllTips()

    @Query("DELETE FROM nutricoachtips WHERE tipId = :tipId")
    suspend fun deleteTipById(tipId: Int)

    @Query("SELECT * FROM nutricoachtips WHERE tipId = :tipId")
    suspend fun getTipById(tipId: Int): NutriCoachTips?

    @Query("SELECT * FROM nutricoachtips WHERE userId = :userId ORDER BY tipId DESC")
    suspend fun getTipsByUserId(userId: Int): List<NutriCoachTips>

    @Query("SELECT * FROM nutricoachtips ORDER BY tipId ASC")
    suspend fun getAllTips(): List<NutriCoachTips>

    suspend fun countTips(): Int {
        return getAllTips().size
    }

    suspend fun insertMultipleTips(tips: List<NutriCoachTips>) {
        for (tip in tips) {
            insert(tip)
        }
    }
}