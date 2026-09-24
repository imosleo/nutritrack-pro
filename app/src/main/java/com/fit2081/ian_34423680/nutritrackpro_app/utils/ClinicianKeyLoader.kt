package com.fit2081.ian_34423680.nutritrackpro_app.utils

import android.content.Context
import com.fit2081.ian_34423680.nutritrackpro_app.database.NutritrackDatabase
import com.fit2081.ian_34423680.nutritrackpro_app.entity.ClinicianKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ClinicianKeyLoader {
    private const val PREF_NAME = "clinician_key_pref"
    private const val KEY_LOADED_FLAG = "clinician_key_loaded"

    fun loadKeyIfFirstRun(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val alreadyLoaded = prefs.getBoolean(KEY_LOADED_FLAG, false)

        if (!alreadyLoaded) {
            CoroutineScope(Dispatchers.IO).launch {
                val dao = NutritrackDatabase.getDatabase(context).clinicianKeyDao()
                if (dao.getKey() == null) {
                    val hashed = SecurityUtils.hashPassword("dollar-entry-apples")
                    dao.insertKey(ClinicianKey(key = hashed))
                }
                prefs.edit().putBoolean(KEY_LOADED_FLAG, true).apply()
            }
        }
    }
}
