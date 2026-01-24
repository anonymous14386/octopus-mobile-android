package com.octopustechnology.octopusapps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                containerColor = Color(0xFF2D2D2D),
                tonalElevation = 0.dp,
                modifier = Modifier.height(56.dp)
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Budget", modifier = Modifier.size(20.dp)) },
                    label = { Text("Budget", fontSize = 11.sp) },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF667EEA),
                        selectedTextColor = Color(0xFF667EEA),
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Health", modifier = Modifier.size(20.dp)) },
                    label = { Text("Health", fontSize = 11.sp) },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF28A745),
                        selectedTextColor = Color(0xFF28A745),
                        indicatorColor = Color.Transparent
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
