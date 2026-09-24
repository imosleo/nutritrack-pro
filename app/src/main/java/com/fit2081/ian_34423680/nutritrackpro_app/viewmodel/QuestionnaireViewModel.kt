package com.fit2081.ian_34423680.nutritrackpro_app.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.ian_34423680.nutritrackpro_app.entity.Questionnaire
import com.fit2081.ian_34423680.nutritrackpro_app.repository.FoodIntakeRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class QuestionnaireViewModel(
    private val repository: FoodIntakeRepository
) : ViewModel() {

    val personaOptions = listOf(
        "Health Devotee", "Mindful Eater", "Wellness Striver",
        "Balance Seeker", "Health Procrastinator", "Food Carefree"
    )
    val foodOptions = listOf(
        "Fruits", "Vegetables", "Grains", "Red Meat", "Seafood",
        "Poultry", "Fish", "Eggs", "Nuts/Seeds"
    )

    var mealTime = mutableStateOf("00:00")
    var sleepTime = mutableStateOf("00:00")
    var wakeTime = mutableStateOf("00:00")
    var persona = mutableStateOf("")
    val selectedFoods = mutableStateMapOf<String, Boolean>()
    private val _questionnaire = MutableStateFlow<Questionnaire?>(null)
    val questionnaire: StateFlow<Questionnaire?> = _questionnaire

    init {
        foodOptions.forEach { selectedFoods[it] = false }
    }

    fun setMealTime(time: String) { mealTime.value = time }
    fun setSleepTime(time: String) { sleepTime.value = time }
    fun setWakeTime(time: String) { wakeTime.value = time }
    fun setPersona(p: String) { persona.value = p }
    fun toggleFood(food: String) {
        selectedFoods[food] = !(selectedFoods[food] ?: false)
    }


    fun loadQuestionnaireByUserId(userId: String) {
        viewModelScope.launch {
            _questionnaire.value = repository.getQuestionnaireByUserId(userId)
        }
    }

    fun save(userId: String, onSaved: () -> Unit) {
        viewModelScope.launch {
            val questionnaire = Questionnaire(
                userId = userId,
                mealTime = mealTime.value,
                sleepTime = sleepTime.value,
                wakeTime = wakeTime.value,
                persona = persona.value,
                foodPrefs = selectedFoods.filterValues { it }.keys.joinToString(",")
            )
            repository.insert(questionnaire)
            onSaved()
        }
    }


    fun load(userId: String) {
        viewModelScope.launch {
            val q = repository.getByUserId(userId)
            q?.let {
                mealTime.value = it.mealTime ?: "00:00"
                sleepTime.value = it.sleepTime ?: "00:00"
                wakeTime.value = it.wakeTime ?: "00:00"
                persona.value = it.persona ?: ""
                foodOptions.forEach { food ->
                    selectedFoods[food] = it.foodPrefs?.split(",")?.contains(food) == true
                }
            }
        }
    }
}