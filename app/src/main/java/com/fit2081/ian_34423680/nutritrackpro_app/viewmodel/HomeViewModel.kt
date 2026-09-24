package com.fit2081.ian_34423680.nutritrackpro_app.viewmodel

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val patientViewModel: PatientViewModel
) : ViewModel() {

    private val _userId = MutableStateFlow("Unknown")
    val userId: StateFlow<String> = _userId.asStateFlow()

    private val _userName = MutableStateFlow("Unknown")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _foodScore = MutableStateFlow("N/A")
    val foodScore: StateFlow<String> = _foodScore.asStateFlow()

    init {
        // Observe currentPatient from PatientViewModel
        viewModelScope.launch {
            snapshotFlow { patientViewModel.currentPatient }
                .collect { patient ->
                    if (patient != null) {
                        _userId.value = patient.userId.toString()
                        _userName.value = patient.name
                        _foodScore.value = "${patient.HEIFAtotalscore}/100"
                    } else {
                        _userId.value = "Unknown"
                        _userName.value = "Unknown"
                        _foodScore.value = "N/A"
                    }
                }
        }
    }
}