package com.fit2081.ian_34423680.nutritrackpro_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.ian_34423680.nutritrackpro_app.entity.Patient
import com.fit2081.ian_34423680.nutritrackpro_app.repository.PatientRepository
import com.fit2081.ian_34423680.nutritrackpro_app.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: PatientRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _currentPatient = MutableStateFlow<Patient?>(null)
    val currentPatient: StateFlow<Patient?> = _currentPatient

    init {
        loadLoggedInPatient()
    }

    private fun loadLoggedInPatient() {
        val userId = sessionManager.getUserId()
        if (userId != null) {
            viewModelScope.launch {
                val patient = repository.getById(userId)
                _currentPatient.value = patient
            }
        }
    }

    fun logout() {
        sessionManager.logout()
    }

    fun updateName(newName: String, onResult: (Boolean) -> Unit) {
        val userId = sessionManager.getUserId()
        if (userId != null) {
            viewModelScope.launch {
                val patient = repository.getById(userId)
                if (patient != null) {
                    val updated = patient.copy(name = newName)
                    repository.insert(updated)
                    _currentPatient.value = updated
                    onResult(true)
                } else {
                    onResult(false)
                }
            }
        } else {
            onResult(false)
        }
    }

    fun updatePassword(newPassword: String, onResult: (Boolean) -> Unit) {
        val userId = sessionManager.getUserId()
        if (userId != null) {
            viewModelScope.launch {
                val patient = repository.getById(userId)
                if (patient != null) {
                    val updated = patient.copy(password = newPassword)
                    repository.insert(updated)
                    _currentPatient.value = updated
                    onResult(true)
                } else {
                    onResult(false)
                }
            }
        } else {
            onResult(false)
        }
    }
}