

package com.octopustechnology.octopusapps.data

data class Goal(
    val id: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val deadline: String? = null,
    val completed: Boolean = false
)

data class WeightEntry(
    val id: Int? = null,
    val date: String,
    val weight: Double,
    val notes: String? = null
)

data class Meal(
    val id: Int? = null,
    val date: String,
    val time: String? = null,
    val description: String,
    val calories: Int? = null,
    val notes: String? = null
)

data class Exercise(
    val id: Int? = null,
    val date: String,
    val type: String,
    val duration: Int,
    val calories_burned: Int? = null,
    val notes: String? = null
)
