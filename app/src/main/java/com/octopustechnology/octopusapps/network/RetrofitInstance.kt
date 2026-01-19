package com.octopustechnology.octopusapps.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BUDGET_BASE_URL = "https://budget.octopustechnology.net/"
    private const val HEALTH_BASE_URL = "https://health.octopustechnology.net/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val budgetApi: BudgetApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BUDGET_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BudgetApiService::class.java)
    }

    val healthApi: HealthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(HEALTH_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HealthApiService::class.java)
    }
}
