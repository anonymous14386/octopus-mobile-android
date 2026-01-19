package com.octopustechnology.octopusapps.data

data class RegisterRequest(
    val username: String,
    val password: String
)

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val userId: String? = null,
    val token: String? = null
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null
)