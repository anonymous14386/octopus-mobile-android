# Android Data Models Reference

This file contains Kotlin data classes that match your API responses. Copy these into your Android project.

## Location in Android Project
Place these in: `app/src/main/java/com/octopus/apps/data/model/`

---

## Common Models

### API Response Wrapper
```kotlin
package com.octopus.apps.data.model

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: String? = null
)
```

### Auth Models
```kotlin
package com.octopus.apps.data.model.auth

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val success: Boolean,
    val token: String,
    val username: String
)
```

---

## Budget Models

### Subscription
```kotlin
package com.octopus.apps.data.model.budget

data class Subscription(
    val id: Int,
    val name: String,
    val amount: Double,
    val frequency: String // "daily", "weekly", "monthly", "yearly"
)

data class CreateSubscriptionRequest(
    val name: String,
    val amount: Double,
    val frequency: String
)
```

### Account
```kotlin
package com.octopus.apps.data.model.budget

data class Account(
    val id: Int,
    val name: String,
    val balance: Double
)

data class CreateAccountRequest(
    val name: String,
    val balance: Double
)
```

### Income
```kotlin
package com.octopus.apps.data.model.budget

data class Income(
    val id: Int,
    val amount: Double,
    val frequency: String // "weekly", "biweekly", "monthly"
)

data class CreateIncomeRequest(
    val amount: Double,
    val frequency: String
)
```

### Debt
```kotlin
package com.octopus.apps.data.model.budget

data class Debt(
    val id: Int,
    val name: String,
    val balance: Double
)

data class CreateDebtRequest(
    val name: String,
    val balance: Double
)
```

### Budget Summary
```kotlin
package com.octopus.apps.data.model.budget

data class BudgetSummary(
    val subscriptions: List<Subscription>,
    val accounts: List<Account>,
    val income: List<Income>,
    val debts: List<Debt>
)
```

---

## Health Models

### Weight Entry
```kotlin
package com.octopus.apps.data.model.health

data class WeightEntry(
    val id: Int,
    val date: String, // "YYYY-MM-DD"
    val weight: Double,
    val notes: String?
)

data class CreateWeightRequest(
    val date: String,
    val weight: Double,
    val notes: String? = null
)
```

### Exercise
```kotlin
package com.octopus.apps.data.model.health

data class Exercise(
    val id: Int,
    val date: String, // "YYYY-MM-DD"
    val type: String,
    val duration: Int, // minutes
    val notes: String?
)

data class CreateExerciseRequest(
    val date: String,
    val type: String,
    val duration: Int,
    val notes: String? = null
)
```

### Meal
```kotlin
package com.octopus.apps.data.model.health

data class Meal(
    val id: Int,
    val date: String, // "YYYY-MM-DD"
    val time: String?, // "HH:MM"
    val description: String,
    val calories: Int?,
    val notes: String?
)

data class CreateMealRequest(
    val date: String,
    val time: String? = null,
    val description: String,
    val calories: Int? = null,
    val notes: String? = null
)
```

### Goal
```kotlin
package com.octopus.apps.data.model.health

data class Goal(
    val id: Int,
    val title: String,
    val description: String?,
    val deadline: String?, // "YYYY-MM-DD"
    val completed: Boolean
)

data class CreateGoalRequest(
    val title: String,
    val description: String? = null,
    val deadline: String? = null,
    val completed: Boolean = false
)
```

### Health Summary
```kotlin
package com.octopus.apps.data.model.health

data class HealthSummary(
    val weight: List<WeightEntry>,
    val exercises: List<Exercise>,
    val meals: List<Meal>,
    val goals: List<Goal>
)
```

---

## Retrofit API Interfaces

### Budget API Service
```kotlin
package com.octopus.apps.data.api

import com.octopus.apps.data.model.*
import com.octopus.apps.data.model.auth.*
import com.octopus.apps.data.model.budget.*
import retrofit2.Response
import retrofit2.http.*

interface BudgetApiService {
    // Auth
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    // Subscriptions
    @GET("budget/subscriptions")
    suspend fun getSubscriptions(): Response<ApiResponse<List<Subscription>>>
    
    @POST("budget/subscriptions")
    suspend fun createSubscription(@Body request: CreateSubscriptionRequest): Response<ApiResponse<Subscription>>
    
    @PUT("budget/subscriptions/{id}")
    suspend fun updateSubscription(
        @Path("id") id: Int,
        @Body request: CreateSubscriptionRequest
    ): Response<ApiResponse<Subscription>>
    
    @DELETE("budget/subscriptions/{id}")
    suspend fun deleteSubscription(@Path("id") id: Int): Response<ApiResponse<Unit>>
    
    // Accounts
    @GET("budget/accounts")
    suspend fun getAccounts(): Response<ApiResponse<List<Account>>>
    
    @POST("budget/accounts")
    suspend fun createAccount(@Body request: CreateAccountRequest): Response<ApiResponse<Account>>
    
    @PUT("budget/accounts/{id}")
    suspend fun updateAccount(
        @Path("id") id: Int,
        @Body request: CreateAccountRequest
    ): Response<ApiResponse<Account>>
    
    @DELETE("budget/accounts/{id}")
    suspend fun deleteAccount(@Path("id") id: Int): Response<ApiResponse<Unit>>
    
    // Income
    @GET("budget/income")
    suspend fun getIncome(): Response<ApiResponse<List<Income>>>
    
    @POST("budget/income")
    suspend fun createIncome(@Body request: CreateIncomeRequest): Response<ApiResponse<Income>>
    
    @PUT("budget/income/{id}")
    suspend fun updateIncome(
        @Path("id") id: Int,
        @Body request: CreateIncomeRequest
    ): Response<ApiResponse<Income>>
    
    @DELETE("budget/income/{id}")
    suspend fun deleteIncome(@Path("id") id: Int): Response<ApiResponse<Unit>>
    
    // Debts
    @GET("budget/debts")
    suspend fun getDebts(): Response<ApiResponse<List<Debt>>>
    
    @POST("budget/debts")
    suspend fun createDebt(@Body request: CreateDebtRequest): Response<ApiResponse<Debt>>
    
    @PUT("budget/debts/{id}")
    suspend fun updateDebt(
        @Path("id") id: Int,
        @Body request: CreateDebtRequest
    ): Response<ApiResponse<Debt>>
    
    @DELETE("budget/debts/{id}")
    suspend fun deleteDebt(@Path("id") id: Int): Response<ApiResponse<Unit>>
    
    // Summary
    @GET("budget/summary")
    suspend fun getBudgetSummary(): Response<ApiResponse<BudgetSummary>>
}
```

### Health API Service
```kotlin
package com.octopus.apps.data.api

import com.octopus.apps.data.model.*
import com.octopus.apps.data.model.auth.*
import com.octopus.apps.data.model.health.*
import retrofit2.Response
import retrofit2.http.*

interface HealthApiService {
    // Auth
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    // Weight
    @GET("health/weight")
    suspend fun getWeightEntries(): Response<ApiResponse<List<WeightEntry>>>
    
    @POST("health/weight")
    suspend fun createWeightEntry(@Body request: CreateWeightRequest): Response<ApiResponse<WeightEntry>>
    
    @PUT("health/weight/{id}")
    suspend fun updateWeightEntry(
        @Path("id") id: Int,
        @Body request: CreateWeightRequest
    ): Response<ApiResponse<WeightEntry>>
    
    @DELETE("health/weight/{id}")
    suspend fun deleteWeightEntry(@Path("id") id: Int): Response<ApiResponse<Unit>>
    
    // Exercises
    @GET("health/exercises")
    suspend fun getExercises(): Response<ApiResponse<List<Exercise>>>
    
    @POST("health/exercises")
    suspend fun createExercise(@Body request: CreateExerciseRequest): Response<ApiResponse<Exercise>>
    
    @PUT("health/exercises/{id}")
    suspend fun updateExercise(
        @Path("id") id: Int,
        @Body request: CreateExerciseRequest
    ): Response<ApiResponse<Exercise>>
    
    @DELETE("health/exercises/{id}")
    suspend fun deleteExercise(@Path("id") id: Int): Response<ApiResponse<Unit>>
    
    // Meals
    @GET("health/meals")
    suspend fun getMeals(): Response<ApiResponse<List<Meal>>>
    
    @POST("health/meals")
    suspend fun createMeal(@Body request: CreateMealRequest): Response<ApiResponse<Meal>>
    
    @PUT("health/meals/{id}")
    suspend fun updateMeal(
        @Path("id") id: Int,
        @Body request: CreateMealRequest
    ): Response<ApiResponse<Meal>>
    
    @DELETE("health/meals/{id}")
    suspend fun deleteMeal(@Path("id") id: Int): Response<ApiResponse<Unit>>
    
    // Goals
    @GET("health/goals")
    suspend fun getGoals(): Response<ApiResponse<List<Goal>>>
    
    @POST("health/goals")
    suspend fun createGoal(@Body request: CreateGoalRequest): Response<ApiResponse<Goal>>
    
    @PUT("health/goals/{id}")
    suspend fun updateGoal(
        @Path("id") id: Int,
        @Body request: CreateGoalRequest
    ): Response<ApiResponse<Goal>>
    
    @DELETE("health/goals/{id}")
    suspend fun deleteGoal(@Path("id") id: Int): Response<ApiResponse<Unit>>
    
    // Summary
    @GET("health/summary")
    suspend fun getHealthSummary(): Response<ApiResponse<HealthSummary>>
}
```

---

## Retrofit Setup with Authentication

### Auth Interceptor
```kotlin
package com.octopus.apps.data.api

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager // You'll create this for DataStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenManager.getToken().first()
        }
        
        val request = chain.request().newBuilder()
        token?.let {
            request.addHeader("Authorization", "Bearer $it")
        }
        
        return chain.proceed(request.build())
    }
}
```

### Retrofit Module (Hilt)
```kotlin
package com.octopus.apps.di

import com.octopus.apps.BuildConfig
import com.octopus.apps.data.api.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BudgetRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HealthRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }
    
    @Provides
    @Singleton
    @BudgetRetrofit
    fun provideBudgetRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConfig.BUDGET_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    @HealthRetrofit
    fun provideHealthRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConfig.HEALTH_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideBudgetApiService(@BudgetRetrofit retrofit: Retrofit): BudgetApiService {
        return retrofit.create(BudgetApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideHealthApiService(@HealthRetrofit retrofit: Retrofit): HealthApiService {
        return retrofit.create(HealthApiService::class.java)
    }
}
```

### Token Manager (DataStore)
```kotlin
package com.octopus.apps.data.api

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    private val USERNAME_KEY = stringPreferencesKey("username")
    
    suspend fun saveToken(token: String, username: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[USERNAME_KEY] = username
        }
    }
    
    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }
    
    fun getUsername(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USERNAME_KEY]
        }
    }
    
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
```

---

## Usage Example in Repository

```kotlin
package com.octopus.apps.data.repository

import com.octopus.apps.data.api.BudgetApiService
import com.octopus.apps.data.api.TokenManager
import com.octopus.apps.data.model.auth.LoginRequest
import com.octopus.apps.data.model.budget.CreateSubscriptionRequest
import javax.inject.Inject

class BudgetRepository @Inject constructor(
    private val api: BudgetApiService,
    private val tokenManager: TokenManager
) {
    suspend fun login(username: String, password: String): Result<String> {
        return try {
            val response = api.login(LoginRequest(username, password))
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                tokenManager.saveToken(authResponse.token, authResponse.username)
                Result.success(authResponse.token)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getSubscriptions() = try {
        val response = api.getSubscriptions()
        if (response.isSuccessful && response.body()?.success == true) {
            Result.success(response.body()!!.data ?: emptyList())
        } else {
            Result.failure(Exception(response.body()?.error ?: "Unknown error"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    // Add more methods...
}
```

---

## Notes

- All these models match your backend API structure
- Use `suspend` functions for coroutines
- Retrofit handles JSON serialization automatically
- DataStore provides secure token storage
- Hilt provides dependency injection
- Copy and adapt these to your project structure

These models will give you a head start when working with GitHub Copilot!
