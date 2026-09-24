package com.fit2081.ian_34423680.nutritrackpro_app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081.ian_34423680.nutritrackpro_app.entity.Fruit
import com.fit2081.ian_34423680.nutritrackpro_app.entity.NutriCoachTips
import com.fit2081.ian_34423680.nutritrackpro_app.entity.Patient
import com.fit2081.ian_34423680.nutritrackpro_app.repository.NutriCoachTipsRepository
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.fit2081.ian_34423680.nutritrackpro_app.utils.FruitApi
import com.fit2081.ian_34423680.nutritrackpro_app.utils.SessionManager
import com.fit2081.ian_34423680.nutritrackpro_app.repository.PatientRepository
import com.fit2081.ian_34423680.nutritrackpro_app.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers

class NutriCoachViewModel(
    private val patientsRepository: PatientRepository,
    private val tipRepository: NutriCoachTipsRepository
) : ViewModel() {

    var userId by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var userData by mutableStateOf<Patient?>(null)
        private set

    var fruit by mutableStateOf("")
        private set

    var fruitData by mutableStateOf<Fruit?>(null)
        private set

    var generatedTip by mutableStateOf<NutriCoachTips?>(null)
        private set

    var isUserOptimal by mutableStateOf(false)
        private set

    var showTipsDialog by mutableStateOf(false)
        private set

    var tipHistory by mutableStateOf<List<NutriCoachTips>>(emptyList())
        private set

    var motivationalMessageLoading by mutableStateOf(false)
        private set

    var generateimagelink by mutableStateOf("")
        private set

    init {
        loadUserData()
    }

    fun refreshAll() {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                isLoading = true
                userId = SessionManager.getLoggedUser() ?: "1"
                userData = patientsRepository.getPatientById(userId.toInt())
                checkUser()
                loadTipHistory()
                generateimagelink = generateRandomImageUrl()
            } catch (_: Exception) {
            } finally {
                isLoading = false
            }
        }
    }

    fun checkUser() {
        userData?.let {
            isUserOptimal = (it.FruitServingScore >= 2) && (it.FruitVariationsScore >= 5)
        }
    }

    fun loadTipHistory() {
        viewModelScope.launch {
            try {
                isLoading = true
                tipHistory = tipRepository.getTipsByUserId(userId.toInt())
            } catch (_: Exception) {
            } finally {
                isLoading = false
            }
        }
    }

    fun toggleTipDialog() {
        showTipsDialog = !showTipsDialog
    }

    fun updateFruit(fruitName: String) {
        fruit = fruitName
    }

    fun findFruit() {
        viewModelScope.launch {
            try {
                if (fruit.isBlank()) return@launch

                isLoading = true

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://fruityvice.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val apiService = retrofit.create(FruitApi::class.java)
                fruitData = apiService.getFruitByName(fruit.trim())

            } catch (_: Exception) {
            } finally {
                isLoading = false
            }
        }
    }

    fun generateTip() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                motivationalMessageLoading = true

                val model = GenerativeModel(
                    modelName = "gemini-1.5-flash",
                    apiKey = BuildConfig.apiKey
                )

                val prompt =
                    """
                    You are a cheerful, bubbly anime nutritionist modeled after Hatsune Miku 💙. 
                    Speak like Miku would: high-energy, positive, slightly dreamy and adorable. 
                    Begin every message with "Senpai~" and end every sentence with "nya~". 
                    Use cute emojis as you like and make it as playful as you want 
                    Include actual Hatsune Miku quotes or references in your speech when relevant.

                    Some examples of Miku quotes you can use or echo:
                    - “Ready, set and jump! Leap up and dance“
                    - “Useless worries, let’s leave them in the dust“
                    - “What if’s and if only’s, they don’t matter“
                    - “Let’s just do what we want, hello hello“
                    - “Don't let the perfect future you give the current you a hard time”
                    - “It's alright, better than I imagined”

                    The goal is to give the patient a short (~50 words) motivational health message that caters specifically to them.

                    Use the following patient data:
                    - Fruit HEIFA score: ${userData?.FruitHEIFAscore}
                    - Vegetable HEIFA score: ${userData?.VegetablesHEIFAscore}
                    (Note: Higher scores = better. Maximum HEIFA score is 10.0.)

                    Speak like you’re cheering them on from a virtual concert stage nya~. 
                    Be playful, wholesome and a little magical nya~.
                    """.trimIndent()

                val response = model.generateContent(prompt)
                val responseText = response.text ?: return@launch

                generatedTip = NutriCoachTips(
                    tipId = 0,
                    content = responseText,
                    userId = userId.toInt()
                )

                tipRepository.insert(generatedTip!!)
                loadTipHistory()

            } catch (_: Exception) {
            } finally {
                motivationalMessageLoading = false
            }
        }
    }

    fun refreshImage() {
        generateimagelink = generateRandomImageUrl()
    }

    fun generateRandomImageUrl(): String {
        return "https://picsum.photos/400?random=${System.currentTimeMillis()}"
    }
}
