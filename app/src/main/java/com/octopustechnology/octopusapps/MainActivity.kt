package com.octopustechnology.octopusapps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.octopustechnology.octopusapps.network.RetrofitInstance
import com.octopustechnology.octopusapps.ui.LoginScreen
import com.octopustechnology.octopusapps.ui.MainScreen
import com.octopustechnology.octopusapps.ui.theme.OctopusAppsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OctopusAppsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Single token works for all services (shared JWT secret)
                    var token by remember { mutableStateOf<String?>(null) }
                    
                    if (token == null) {
                        LoginScreen(
                            authApi = RetrofitInstance.authApi,
                            onLoginSuccess = { newToken ->
                                token = newToken
                            }
                        )
                    } else {
                        MainScreen(
                            token = token!!,
                            budgetApi = RetrofitInstance.budgetApi,
                            healthApi = RetrofitInstance.healthApi,
                            onLogout = {
                                token = null
                            }
                        )
                    }
                }
            }
        }
    }
}