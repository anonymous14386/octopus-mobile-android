package com.octopustechnology.octopusapps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.octopustechnology.octopusapps.data.*
import kotlinx.coroutines.launch

@Composable
fun AddHealthItemDialog(
    type: String,
    api: com.octopustechnology.octopusapps.network.HealthApiService,
    token: String,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    // Simple input fields for demo; expand as needed
    var value1 by remember { mutableStateOf("") }
    var value2 by remember { mutableStateOf("") }
    var value3 by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Add ${type.replaceFirstChar { it.uppercase() }}")
        },
        text = {
            Column {
                when (type) {
                    "weight" -> {
                        OutlinedTextField(value = value1, onValueChange = { value1 = it }, label = { Text("Weight (lbs)") })
                        OutlinedTextField(value = value2, onValueChange = { value2 = it }, label = { Text("Notes") })
                    }
                    "exercise" -> {
                        OutlinedTextField(value = value1, onValueChange = { value1 = it }, label = { Text("Type") })
                        OutlinedTextField(value = value2, onValueChange = { value2 = it }, label = { Text("Minutes") })
                        OutlinedTextField(value = value3, onValueChange = { value3 = it }, label = { Text("Calories Burned") })
                    }
                    "meal" -> {
                        OutlinedTextField(value = value1, onValueChange = { value1 = it }, label = { Text("Description") })
                        OutlinedTextField(value = value2, onValueChange = { value2 = it }, label = { Text("Calories") })
                    }
                    "goal" -> {
                        OutlinedTextField(value = value1, onValueChange = { value1 = it }, label = { Text("Goal Description") })
                    }
                }
                if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    isLoading = true
                    errorMessage = ""
                    val authHeader = "Bearer $token"
                    val today = java.time.LocalDate.now().toString()
                    when (type) {
                        "weight" -> {
                            val entry = WeightEntry(date = today, weight = value1.toDoubleOrNull() ?: 0.0, notes = value2)
                            try {
                                kotlinx.coroutines.GlobalScope.launch {
                                    try {
                                        api.addWeight(authHeader, entry)
                                        onSuccess()
                                    } catch (e: Exception) {
                                        errorMessage = e.message ?: "Failed to add weight"
                                    } finally { isLoading = false }
                                }
                            } catch (e: Exception) { errorMessage = e.message ?: "Failed to add weight"; isLoading = false }
                        }
                        "exercise" -> {
                            val entry = Exercise(date = today, exercise_type = value1, duration_minutes = value2.toIntOrNull() ?: 0, calories_burned = value3.toIntOrNull())
                            try {
                                kotlinx.coroutines.GlobalScope.launch {
                                    try {
                                        api.addExercise(authHeader, entry)
                                        onSuccess()
                                    } catch (e: Exception) {
                                        errorMessage = e.message ?: "Failed to add exercise"
                                    } finally { isLoading = false }
                                }
                            } catch (e: Exception) { errorMessage = e.message ?: "Failed to add exercise"; isLoading = false }
                        }
                        "meal" -> {
                            val entry = Meal(date = today, meal_type = "", description = value1, calories = value2.toIntOrNull(), notes = null)
                            try {
                                kotlinx.coroutines.GlobalScope.launch {
                                    try {
                                        api.addMeal(authHeader, entry)
                                        onSuccess()
                                    } catch (e: Exception) {
                                        errorMessage = e.message ?: "Failed to add meal"
                                    } finally { isLoading = false }
                                }
                            } catch (e: Exception) { errorMessage = e.message ?: "Failed to add meal"; isLoading = false }
                        }
                        "goal" -> {
                            val entry = Goal(title = value1, description = value1)
                            try {
                                kotlinx.coroutines.GlobalScope.launch {
                                    try {
                                        api.addGoal(authHeader, entry)
                                        onSuccess()
                                    } catch (e: Exception) {
                                        errorMessage = e.message ?: "Failed to add goal"
                                    } finally { isLoading = false }
                                }
                            } catch (e: Exception) { errorMessage = e.message ?: "Failed to add goal"; isLoading = false }
                        }
                    }
                },
                enabled = !isLoading
            ) { Text("Add") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
