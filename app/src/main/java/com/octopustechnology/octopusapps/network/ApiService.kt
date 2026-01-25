package com.octopustechnology.octopusapps.network

import com.octopustechnology.octopusapps.data.*
import retrofit2.http.*

// Centralized Auth Service (optional, can use budget/health proxies instead)
interface AuthApiService {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}

interface BudgetApiService {
    // Auth endpoints (proxied to octopus-auth)
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("api/subscriptions")
    suspend fun getSubscriptions(@Header("Authorization") token: String): List<Subscription>
    
    @POST("api/subscriptions")
    suspend fun addSubscription(@Header("Authorization") token: String, @Body subscription: Subscription): Subscription
    
    @DELETE("api/subscriptions/{id}")
    suspend fun deleteSubscription(@Header("Authorization") token: String, @Path("id") id: Int): DeleteResponse
    
    @GET("api/accounts")
    suspend fun getAccounts(@Header("Authorization") token: String): List<Account>
    
    @POST("api/accounts")
    suspend fun addAccount(@Header("Authorization") token: String, @Body account: Account): Account
    
    @DELETE("api/accounts/{id}")
    suspend fun deleteAccount(@Header("Authorization") token: String, @Path("id") id: Int): DeleteResponse
    
    @GET("api/income")
    suspend fun getIncome(@Header("Authorization") token: String): List<Income>
    
    @POST("api/income")
    suspend fun addIncome(@Header("Authorization") token: String, @Body income: Income): Income
    
    @DELETE("api/income/{id}")
    suspend fun deleteIncome(@Header("Authorization") token: String, @Path("id") id: Int): DeleteResponse
    
    @GET("api/debts")
    suspend fun getDebts(@Header("Authorization") token: String): List<Debt>
    
    @POST("api/debts")
    suspend fun addDebt(@Header("Authorization") token: String, @Body debt: Debt): Debt

    @PUT("api/debts/{id}")
    suspend fun updateDebt(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body debt: Debt
    ): Debt
    
    @DELETE("api/debts/{id}")
    suspend fun deleteDebt(@Header("Authorization") token: String, @Path("id") id: Int): DeleteResponse
}

interface HealthApiService {
    // Weight
    @GET("api/health/weight")
    suspend fun getWeights(@Header("Authorization") token: String): ApiListResponse<WeightEntry>

    @POST("api/health/weight")
    suspend fun addWeight(@Header("Authorization") token: String, @Body entry: WeightEntry): ApiSingleResponse<WeightEntry>
    
    @DELETE("api/health/weight/{id}")
    suspend fun deleteWeight(@Header("Authorization") token: String, @Path("id") id: Int): DeleteResponse

    // Exercise
    @GET("api/health/exercises")
    suspend fun getExercises(@Header("Authorization") token: String): ApiListResponse<Exercise>

    @POST("api/health/exercises")
    suspend fun addExercise(@Header("Authorization") token: String, @Body entry: Exercise): ApiSingleResponse<Exercise>
    
    @DELETE("api/health/exercises/{id}")
    suspend fun deleteExercise(@Header("Authorization") token: String, @Path("id") id: Int): DeleteResponse

    // Meals
    @GET("api/health/meals")
    suspend fun getMeals(@Header("Authorization") token: String): ApiListResponse<Meal>

    @POST("api/health/meals")
    suspend fun addMeal(@Header("Authorization") token: String, @Body entry: Meal): ApiSingleResponse<Meal>
    
    @DELETE("api/health/meals/{id}")
    suspend fun deleteMeal(@Header("Authorization") token: String, @Path("id") id: Int): DeleteResponse

    // Goals
    @GET("api/health/goals")
    suspend fun getGoals(@Header("Authorization") token: String): ApiListResponse<Goal>

    @POST("api/health/goals")
    suspend fun addGoal(@Header("Authorization") token: String, @Body entry: Goal): ApiSingleResponse<Goal>
    
    @DELETE("api/health/goals/{id}")
    suspend fun deleteGoal(@Header("Authorization") token: String, @Path("id") id: Int): DeleteResponse
}

// Generic API response wrappers
data class ApiListResponse<T>(val success: Boolean, val data: List<T>)
data class ApiSingleResponse<T>(val success: Boolean, val data: T?)
data class DeleteResponse(val success: Boolean, val message: String? = null)