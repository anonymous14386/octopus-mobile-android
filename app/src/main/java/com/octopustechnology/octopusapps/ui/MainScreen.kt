package com.octopustechnology.octopusapps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.octopustechnology.octopusapps.network.BudgetApiService
import com.octopustechnology.octopusapps.network.HealthApiService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    token: String,
    budgetApi: BudgetApiService,
    healthApi: HealthApiService,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF2D2D2D)
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Budget") },
                    label = { Text("Budget") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF667EEA),
                        selectedTextColor = Color(0xFF667EEA),
                        indicatorColor = Color(0xFF667EEA).copy(alpha = 0.1f)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Health") },
                    label = { Text("Health") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF28A745),
                        selectedTextColor = Color(0xFF28A745),
                        indicatorColor = Color(0xFF28A745).copy(alpha = 0.1f)
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> BudgetScreen(
                    token = token,
                    api = budgetApi,
                    onLogout = onLogout
                )
                1 -> HealthScreen(
                    token = token,
                    api = healthApi,
                    onLogout = onLogout
                )
            }
        }
    }
}
