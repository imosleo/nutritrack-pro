@file:OptIn(ExperimentalMaterial3Api::class)

package com.fit2081.ian_34423680.nutritrackpro_app.screen

import android.app.TimePickerDialog
import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.fit2081.ian_34423680.nutritrackpro_app.navigation.ScreenRoute
import com.fit2081.ian_34423680.nutritrackpro_app.utils.SessionManager
import com.fit2081.ian_34423680.nutritrackpro_app.utils.PersonaData
import com.fit2081.ian_34423680.nutritrackpro_app.viewmodel.QuestionnaireViewModel
import com.google.accompanist.flowlayout.FlowRow
import java.util.*

@Composable
fun QuestionnaireScreen(
    navController: NavHostController,
    viewModel: QuestionnaireViewModel,
    sessionManager: SessionManager,
    userId: String
) {
    val context = LocalContext.current

    val persona by remember { derivedStateOf { viewModel.persona.value } }
    val foodPrefs by remember { derivedStateOf { viewModel.selectedFoods.filterValues { it }.keys } }
    val mealTime by remember { derivedStateOf { viewModel.mealTime.value } }
    val sleepTime by remember { derivedStateOf { viewModel.sleepTime.value } }
    val wakeTime by remember { derivedStateOf { viewModel.wakeTime.value } }

    var expanded by remember { mutableStateOf(false) }
    var selectedPersonaForInfo by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    val foodOptions = viewModel.foodOptions
    val personaOptions = viewModel.personaOptions

    LaunchedEffect(userId) {
        sessionManager.saveSession(userId, ScreenRoute.Questionnaire.route)
        viewModel.load(userId)
    }

    fun showTimePicker(current: String, onSet: (String) -> Unit) {
        val cal = Calendar.getInstance()
        val hour = current.split(":").getOrNull(0)?.toIntOrNull() ?: cal.get(Calendar.HOUR_OF_DAY)
        val min = current.split(":").getOrNull(1)?.toIntOrNull() ?: cal.get(Calendar.MINUTE)
        TimePickerDialog(context, { _: TimePicker, h: Int, m: Int ->
            onSet(String.format("%02d:%02d", h, m))
        }, hour, min, true).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Food Intake Questionnaire", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        SessionManager.setLastRoute(ScreenRoute.Login.route)
                        navController.navigate(ScreenRoute.Login.route){
                            popUpTo("questionnaire") { inclusive = true }
                            launchSingleTop = true
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.persona.value = ""
                        viewModel.mealTime.value = "00:00"
                        viewModel.sleepTime.value = "00:00"
                        viewModel.wakeTime.value = "00:00"
                        viewModel.selectedFoods.keys.forEach { viewModel.selectedFoods[it] = false }
                        Toast.makeText(context, "Questionnaire reset", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6200EE))
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        if (foodPrefs.isEmpty()) {
                            errorMessage = "Please select at least one food category."
                        } else if (persona.isBlank()) {
                            errorMessage = "Please select a persona."
                        } else if (mealTime == sleepTime || sleepTime == wakeTime || mealTime == wakeTime) {
                            errorMessage = "Meal, sleep and wake times must all be different."
                        } else {
                            errorMessage = ""
                            viewModel.save(userId) {
                                SessionManager.setLastRoute(ScreenRoute.Home.route)
                                navController.navigate(ScreenRoute.Home.route)
                            }
                        }
                    },
                    modifier = Modifier.width(140.dp).height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Icon(Icons.Filled.Save, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Save", color = Color.White)
                }
                LaunchedEffect(errorMessage) {
                    if (errorMessage.isNotEmpty()) {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        errorMessage = ""
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 18.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Tick all the food categories you can eat", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 200.dp)
            ) {
                items(foodOptions) { item ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = viewModel.selectedFoods[item] == true,
                            onCheckedChange = { viewModel.toggleFood(item) }
                        )
                        Text(item, fontSize = 12.sp)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("Your Persona", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(
                "People can be broadly classified into 6 different types based on their eating preferences. Click on each button below to find out the different types, and select the type that best fits you!",
                fontSize = 12.sp
            )

            Spacer(Modifier.height(8.dp))
            FlowRow(mainAxisSpacing = 8.dp, crossAxisSpacing = 4.dp) {
                personaOptions.forEach { option ->
                    Button(
                        onClick = {
                            viewModel.setPersona(option)
                            selectedPersonaForInfo = option
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFF6200EE)),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(option, color = Color.White, fontSize = 12.sp)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("Which persona best fits you?", fontWeight = FontWeight.Bold, fontSize = 16.sp)

            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = persona,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Persona") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(24.dp)
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    personaOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                viewModel.setPersona(option)
                                expanded = false
                                selectedPersonaForInfo = option
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Text("Timings", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(12.dp))

            listOf(
                "What time approx. do you normally eat your biggest meal?" to mealTime to { t: String -> viewModel.setMealTime(t) },
                "What time approx. do you go to sleep at night?" to sleepTime to { t: String -> viewModel.setSleepTime(t) },
                "What time approx. do you wake up in the morning?" to wakeTime to { t: String -> viewModel.setWakeTime(t) }
            ).forEach { (labelTime, setter) ->
                val (label, time) = labelTime
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(label, fontSize = 14.sp, modifier = Modifier.weight(1f))
                    OutlinedButton(
                        onClick = {
                            showTimePicker(time) { newTime -> setter(newTime) }
                        },
                        modifier = Modifier.width(160.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Filled.AccessTime, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text(time)
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            selectedPersonaForInfo?.let { selected ->
                val imageRes = selected.lowercase().replace(" ", "_")
                val imageId = context.resources.getIdentifier(imageRes, "drawable", context.packageName)

                AlertDialog(
                    onDismissRequest = { selectedPersonaForInfo = null },
                    confirmButton = {},
                    title = null,
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (imageId != 0) {
                                Image(
                                    painter = painterResource(id = imageId),
                                    contentDescription = selected,
                                    modifier = Modifier.size(120.dp)
                                )
                                Spacer(Modifier.height(10.dp))
                            }
                            Text(selected, fontWeight = FontWeight.Bold, fontSize = 18.sp, textAlign = TextAlign.Center)
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = PersonaData.personaDescriptions[selected] ?: "",
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = { selectedPersonaForInfo = null }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))) {
                                Text("Dismiss", color = Color.White)
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    properties = DialogProperties(dismissOnClickOutside = true)
                )
            }
        }
    }
}