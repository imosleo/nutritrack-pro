package com.fit2081.ian_34423680.nutritrackpro_app.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.ian_34423680.nutritrackpro_app.entity.Patient
import com.fit2081.ian_34423680.nutritrackpro_app.repository.PatientRepository
import com.fit2081.ian_34423680.nutritrackpro_app.utils.SecurityUtils
import kotlinx.coroutines.launch

class PatientViewModel(private val repository: PatientRepository) : ViewModel() {

    var currentPatient by mutableStateOf<Patient?>(null)
        private set

    var loginError by mutableStateOf<String?>(null)
        private set

    var registerSuccess by mutableStateOf<Boolean?>(null)
        private set

    var allUserIds by mutableStateOf<List<String>>(emptyList())
        private set

    var unregisteredUserIds by mutableStateOf<List<String>>(emptyList())
        private set

    init {
        refreshUnregisteredUserIds()
    }

    fun refreshUnregisteredUserIds() {
        viewModelScope.launch {
            val ids = repository.getAllUserIds().map { it.toString() }
            allUserIds = ids
            unregisteredUserIds = ids.filter {
                val idInt = it.toIntOrNull()
                idInt != null && repository.getById(idInt)?.password.isNullOrEmpty()
            }
        }
    }

    fun login(userId: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val idInt = userId.toIntOrNull()
            val user = idInt?.let { repository.getById(it) }
            if (user != null && SecurityUtils.verifyPassword(password, user.password!!)) {
                currentPatient = user
                loginError = null
                onResult(true)
            } else {
                loginError = "Invalid ID or password"
                onResult(false)
            }
        }
    }

    fun register(userId: String, phone: String, name: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val idInt = userId.toIntOrNull()
            val patient = idInt?.let { repository.getById(it) }
            if (patient != null && patient.password.isNullOrEmpty()) {
                val hashed = SecurityUtils.hashPassword(password)
                val updated = patient.copy(name = name, phoneNumber = phone, password = hashed)
                repository.insert(updated)
                currentPatient = updated
                registerSuccess = true
                refreshUnregisteredUserIds()
                onResult(true)
            } else {
                registerSuccess = false
                onResult(false)
            }
        }
    }

    fun setCurrentPatient(userId: String) {
        viewModelScope.launch {
            val idInt = userId.toIntOrNull()
            currentPatient = idInt?.let { repository.getById(it) }
        }
    }
}