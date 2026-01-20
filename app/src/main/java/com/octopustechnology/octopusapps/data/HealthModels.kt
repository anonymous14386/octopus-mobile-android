package com.octopustechnology.octopusapps.data

data class WeightEntry(
    val id: Int? = null,
    val date: String,
    val weight: Double,
    val notes: String? = null
)

data class Meal(
    val id: Int? = null,
    val date: String,
    val meal_type: String,
    val description: String,
    val calories: Int? = null,
    val notes: String? = null
)

data class Exercise(
    val id: Int? = null,
    val date: String,
    val exercise_type: String,
    val duration_minutes: Int,
    val calories_burned: Int? = null,
    val notes: String? = null
)
