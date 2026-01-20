package com.octopustechnology.octopusapps.network

import com.octopustechnology.octopusapps.data.*
import retrofit2.http.*

interface BudgetApiService {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
    
    @GET("api/subscriptions")
    suspend fun getSubscriptions(@Header("Authorization") token: String): List<Subscription>
    
    @GET("api/accounts")
    suspend fun getAccounts(@Header("Authorization") token: String): List<Account>
    
    @GET("api/income")
    suspend fun getIncome(@Header("Authorization") token: String): List<Income>
    
    @GET("api/debts")
    suspend fun getDebts(@Header("Authorization") token: String): List<Debt>
}

interface HealthApiService {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}