package com.fit2081.ian_34423680.nutritrackpro_app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fit2081.ian_34423680.nutritrackpro_app.dao.ClinicianKeyDao
import com.fit2081.ian_34423680.nutritrackpro_app.dao.NutriCoachTipsDao
import com.fit2081.ian_34423680.nutritrackpro_app.dao.PatientDao
import com.fit2081.ian_34423680.nutritrackpro_app.dao.QuestionnaireDao
import com.fit2081.ian_34423680.nutritrackpro_app.entity.ClinicianKey
import com.fit2081.ian_34423680.nutritrackpro_app.entity.Patient
import com.fit2081.ian_34423680.nutritrackpro_app.entity.Questionnaire
import com.fit2081.ian_34423680.nutritrackpro_app.entity.NutriCoachTips

@Database(
    entities = [Patient::class, Questionnaire::class, NutriCoachTips::class, ClinicianKey::class],
    version = 8,
    exportSchema = false
)
abstract class NutritrackDatabase : RoomDatabase() {

    abstract fun patientDao(): PatientDao
    abstract fun questionnaireDao(): QuestionnaireDao
    abstract fun nutriCoachDao(): NutriCoachTipsDao
    abstract fun clinicianKeyDao(): ClinicianKeyDao

    companion object {
        @Volatile
        private var INSTANCE: NutritrackDatabase? = null

        fun getDatabase(context: Context): NutritrackDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NutritrackDatabase::class.java,
                    "nutritrack_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}