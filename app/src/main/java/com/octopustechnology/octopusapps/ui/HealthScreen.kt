package com.octopustechnology.octopusapps.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.octopustechnology.octopusapps.data.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthScreen(
    token: String,
    api: com.octopustechnology.octopusapps.network.HealthApiService,
    onLogout: () -> Unit
) {
    var weights by remember { mutableStateOf<List<WeightEntry>>(emptyList()) }
    var meals by remember { mutableStateOf<List<Meal>>(emptyList()) }
    var exercises by remember { mutableStateOf<List<Exercise>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val loadData = {
        scope.launch {
            isLoading = true
            try {
                val authHeader = "Bearer $token"
                val weightsResp = api.getWeights(authHeader)
                val mealsResp = api.getMeals(authHeader)
                val exercisesResp = api.getExercises(authHeader)
                weights = weightsResp.data
                meals = mealsResp.data
                exercises = exercisesResp.data
                errorMessage = ""
            } catch (e: Exception) {
                errorMessage = "Error loading data: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadData()
    }

    // Calculate stats
    val recentWeight = weights.maxByOrNull { it.date }
    val today = java.time.LocalDate.now().toString()
    val todayExercises = exercises.filter { it.date == today }
    val todayMeals = meals.filter { it.date == today }
    val todayExerciseMinutes = todayExercises.sumOf { it.duration_minutes }
    val todayCalories = todayMeals.sumOf { it.calories ?: 0 }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("\uD83C\uDFE5 Health Tracker", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Logout", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF667EEA)
                )
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
                Button(onClick = { loadData() }, modifier = Modifier.padding(top = 16.dp)) {
                    Text("Retry")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF5F7FA)),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Summary Cards (Grid)
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SummaryCard(
                                title = "⚖️ Current Weight",
                                amount = recentWeight?.weight ?: 0.0,
                                color = Color(0xFF667EEA),
                                subtitle = recentWeight?.let { "Last updated: ${it.date}" } ?: "No weight entries yet",
                                modifier = Modifier.weight(1f)
                            )
                            SummaryCard(
                                title = "🏃 Today's Exercise",
                                amount = todayExerciseMinutes.toDouble(),
                                color = Color(0xFF28A745),
                                subtitle = "${todayExercises.size} activities",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SummaryCard(
                                title = "🍽️ Today's Calories",
                                amount = todayCalories.toDouble(),
                                color = Color(0xFFDC3545),
                                subtitle = "${todayMeals.size} meals logged",
                                modifier = Modifier.weight(1f)
                            )
                            SummaryCard(
                                title = "🎯 Active Goals",
                                amount = 0.0, // TODO: Replace with actual goals count
                                color = Color(0xFF667EEA),
                                subtitle = "goals in progress",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Weight Section
                item {
                    SectionHeader("Log Weight") { showAddDialog = "weight" }
                }
                if (todayExercises.isNotEmpty()) {
                    item {
                        Text("Today's Weights:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
                    }
                    items(weights.filter { it.date == today }) { entry ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Text("${entry.weight} lbs - ${entry.notes ?: "No notes"}", modifier = Modifier.padding(12.dp))
                        }
                    }
                }

                // Exercise Section
                item {
                    SectionHeader("Log Exercise") { showAddDialog = "exercise" }
                }
                if (todayExercises.isNotEmpty()) {
                    item {
                        Text("Today's Exercises:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
                    }
                    items(todayExercises) { entry ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Text("${entry.exercise_type}: ${entry.duration_minutes} min, ${entry.calories_burned ?: 0} cal", modifier = Modifier.padding(12.dp))
                        }
                    }
                }

                // Meal Section
                item {
                    SectionHeader("Log Meal") { showAddDialog = "meal" }
                }
                if (todayMeals.isNotEmpty()) {
                    item {
                        Text("Today's Meals:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
                    }
                    items(todayMeals) { entry ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Text("${entry.description} - ${entry.calories ?: 0} cal", modifier = Modifier.padding(12.dp))
                        }
                    }
                }

                // Goal Section
                item {
                    SectionHeader("Log Goal") { showAddDialog = "goal" }
                }
            }

            // Add Dialogs for logging
            showAddDialog?.let { type ->
                AddHealthItemDialog(
                    type = type,
                    api = api,
                    token = token,
                    onDismiss = { showAddDialog = null },
                    onSuccess = {
                        showAddDialog = null
                        loadData()
                    }
                )
            }
        }
    }
}

