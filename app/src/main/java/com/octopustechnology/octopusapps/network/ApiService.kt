package com.octopustechnology.octopusapps.network

import com.octopustechnology.octopusapps.data.LoginRequest
import com.octopustechnology.octopusapps.data.LoginResponse
import com.octopustechnology.octopusapps.data.RegisterRequest
import com.octopustechnology.octopusapps.data.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface BudgetApiService {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}

interface HealthApiService {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}