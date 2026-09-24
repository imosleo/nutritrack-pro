package com.fit2081.ian_34423680.nutritrackpro_app.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.ian_34423680.nutritrackpro_app.repository.PatientRepository
import com.fit2081.ian_34423680.nutritrackpro_app.utils.SessionManager
import kotlinx.coroutines.launch

class LoginViewModel(
    private val patientRepository: PatientRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    val allUserIds = mutableStateListOf<String>()
    var currentPatient = mutableStateOf<String?>(null)
        private set
    var loginResult = mutableStateOf<Boolean?>(null)
        private set

    init {
        viewModelScope.launch {
            allUserIds.clear()
            val ids = patientRepository.getAllUserIds().map { it.toString() }
            allUserIds.addAll(ids)
        }
    }

    fun login(userId: String, password: String) {
        viewModelScope.launch {
            val idInt = userId.toIntOrNull()
            val success = idInt?.let { patientRepository.validateUser(it, password) } ?: false
            if (success) {
                setCurrentPatient(userId)
                sessionManager.login(userId)
            }
            loginResult.value = success
        }
    }


    fun setCurrentPatient(userId: String) {
        currentPatient.value = userId
    }
}