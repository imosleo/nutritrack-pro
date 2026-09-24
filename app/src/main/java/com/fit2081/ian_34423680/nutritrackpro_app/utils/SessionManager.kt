package com.fit2081.ian_34423680.nutritrackpro_app.utils

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object SessionManager {
    private const val PREF_NAME = "nutritrack_session"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_LAST_ROUTE = "last_route"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"

    private lateinit var appContext: Context

    val _userId: MutableState<String?> = mutableStateOf(null)

    fun initialize(context: Context) {
        appContext = context.applicationContext
        val sharedPref = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val savedUserId = sharedPref.getString(KEY_USER_ID, null)
        if (!savedUserId.isNullOrEmpty()) {
            _userId.value = savedUserId
        }
    }

    fun login(userId: String) {
        _userId.value = userId
        val sharedPref = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(KEY_USER_ID, userId)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun saveSession(userId: String, lastRoute: String? = null) {
        _userId.value = userId
        val sharedPref = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(KEY_USER_ID, userId)
            putBoolean(KEY_IS_LOGGED_IN, true)
            if (lastRoute != null) putString(KEY_LAST_ROUTE, lastRoute)
            apply()
        }
    }

    fun setLastRoute(route: String) {
        val sharedPref = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(KEY_LAST_ROUTE, route)
            apply()
        }
    }

    fun getLastRoute(): String? {
        val sharedPref = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_LAST_ROUTE, null)
    }

    fun isLoggedIn(): Boolean {
        val sharedPref = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserId(): Int? {
        val userIdStr = _userId.value
        val userId = userIdStr?.toIntOrNull()
        if (userId != null) return userId

        val sharedPref = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val savedUserId = sharedPref.getString(KEY_USER_ID, null)?.toIntOrNull()
        if (savedUserId != null) {
            _userId.value = savedUserId.toString()
        }
        return savedUserId
    }

    fun logout() {
        _userId.value = null
        val sharedPref = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear() // ← this removes all: user_id, is_logged_in, last_route
            apply()
        }
    }

    fun getLoggedUser(): String? {
        return _userId.value
    }
}
