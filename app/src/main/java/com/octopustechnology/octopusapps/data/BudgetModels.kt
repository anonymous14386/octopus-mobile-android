package com.octopustechnology.octopusapps.data

data class Subscription(
    val id: Int? = null,
    val name: String,
    val amount: Double,
    val frequency: String,
    val category: String? = null,
    val notes: String? = null
)

data class Account(
    val id: Int? = null,
    val name: String,
    val balance: Double,
    val type: String? = null,
    val notes: String? = null
)

data class Income(
    val id: Int? = null,
    val source: String? = null,
    val amount: Double,
    val frequency: String,
    val notes: String? = null
)

data class Debt(
    val id: Int? = null,
    val name: String,
    val amount: Double,
    val balance: Double? = null,
    val interest_rate: Double? = null,
    val minimum_payment: Double? = null,
    val due_date: String? = null,
    val notes: String? = null
)
