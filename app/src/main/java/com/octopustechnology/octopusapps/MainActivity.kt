package com.octopustechnology.octopusapps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.octopustechnology.octopusapps.data.LoginRequest
import com.octopustechnology.octopusapps.data.RegisterRequest
import com.octopustechnology.octopusapps.network.RetrofitInstance
import com.octopustechnology.octopusapps.ui.theme.OctopusAppsTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OctopusAppsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthScreen()
                }
            }
        }
    }

    @Composable
    fun AuthScreen() {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var selectedApi by remember { mutableStateOf("Budget") }
        var resultMessage by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Octopus Apps",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // API Selection
            Text("Select API:", style = MaterialTheme.typography.bodyLarge)
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FilterChip(
                    selected = selectedApi == "Budget",
                    onClick = { selectedApi = "Budget" },
                    label = { Text("Budget API") }
                )
                FilterChip(
                    selected = selectedApi == "Health",
                    onClick = { selectedApi = "Health" },
                    label = { Text("Health API") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Username field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login button
            Button(
                onClick = {
                    lifecycleScope.launch {
                        isLoading = true
                        resultMessage = ""
                        try {
                            val response = if (selectedApi == "Budget") {
                                RetrofitInstance.budgetApi.login(
                                    LoginRequest(username, password)
                                )
                            } else {
                                RetrofitInstance.healthApi.login(
                                    LoginRequest(username, password)
                                )
                            }
                            resultMessage = "✅ Login Success: message=${response.message}, token=${response.token}, success=${response.success}"
                        } catch (e: Exception) {
                            val errorDetails = if (e is retrofit2.HttpException) {
                                "HTTP ${e.code()}: ${e.response()?.errorBody()?.string()}"
                            } else {
                                e.message ?: "Unknown error"
                            }
                            resultMessage = "❌ Login Error: $errorDetails"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && username.isNotEmpty() && password.isNotEmpty()
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Register button
            OutlinedButton(
                onClick = {
                    lifecycleScope.launch {
                        isLoading = true
                        resultMessage = ""
                        try {
                            val response = if (selectedApi == "Budget") {
                                RetrofitInstance.budgetApi.register(
                                    RegisterRequest(username, password)
                                )
                            } else {
                                RetrofitInstance.healthApi.register(
                                    RegisterRequest(username, password)
                                )
                            }
                            resultMessage = "✅ Register Success: ${response.message}"
                        } catch (e: Exception) {
                            resultMessage = "❌ Register Error: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && username.isNotEmpty() && password.isNotEmpty()
            ) {
                Text("Register")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Loading indicator
            if (isLoading) {
                CircularProgressIndicator()
            }

            // Result message
            if (resultMessage.isNotEmpty()) {
                Text(
                    text = resultMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
