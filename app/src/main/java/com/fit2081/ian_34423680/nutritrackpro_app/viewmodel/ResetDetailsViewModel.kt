package com.fit2081.ian_34423680.nutritrackpro_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.ian_34423680.nutritrackpro_app.entity.Patient
import com.fit2081.ian_34423680.nutritrackpro_app.repository.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ResetDetailsViewModel(private val repository: PatientRepository) : ViewModel() {
    private val _allUserIds = MutableStateFlow<List<String>>(emptyList())
    val allUserIds: StateFlow<List<String>> = _allUserIds

    private val _currentPatient = MutableStateFlow<Patient?>(null)
    val currentPatient: StateFlow<Patient?> = _currentPatient

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _successMessage = MutableStateFlow("")
    val successMessage: StateFlow<String> = _successMessage

    init {
        fetchAllUserIds()
    }

    fun fetchAllUserIds() {
        viewModelScope.launch {
            _allUserIds.value = repository.getAllUserIds().map { it.toString() }
        }
    }

    fun fetchPatientById(userId: String) {
        viewModelScope.launch {
            val idInt = userId.toIntOrNull()
            _currentPatient.value = idInt?.let { repository.getPatientById(it) }
        }
    }

    fun resetField(
        userId: String,
        phone: String,
        field: String,
        newValue: String
    ) {
        viewModelScope.launch {
            val idInt = userId.toIntOrNull()
            val patient = idInt?.let { repository.getPatientById(it) }
            if (patient == null) {
                _errorMessage.value = "User not found."
                _successMessage.value = ""
                return@launch
            }
            if (phone.trim() != patient.phoneNumber) {
                _errorMessage.value = "Phone number does not match our records."
                _successMessage.value = ""
                return@launch
            }
            if (field == "Password") {
                if (!isValidPassword(newValue)) {
                    _errorMessage.value = "Password must include uppercase, lowercase, number, symbol, and be at least 7 characters long."
                    _successMessage.value = ""
                    return@launch
                }
                if (newValue == patient.password) {
                    _errorMessage.value = "New password must be different from the current one."
                    _successMessage.value = ""
                    return@launch
                }
                idInt?.let { repository.updatePassword(it, newValue) }
                _successMessage.value = "Password updated successfully for ID $userId"
                _errorMessage.value = ""
            } else if (field == "Name") {
                if (newValue.equals(patient.name?.trim(), ignoreCase = true)) {
                    _errorMessage.value = "New name must be different from the current one."
                    _successMessage.value = ""
                    return@launch
                }
                idInt?.let { repository.updateName(it, newValue) }
                _successMessage.value = "Name updated successfully for ID $userId"
                _errorMessage.value = ""
            }
            fetchPatientById(userId)
        }
    }

    fun clearMessages() {
        _errorMessage.value = ""
        _successMessage.value = ""
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 7 &&
                password.any { it.isUpperCase() } &&
                password.any { it.isLowerCase() } &&
                password.any { it.isDigit() } &&
                password.any { !it.isLetterOrDigit() }
    }
}