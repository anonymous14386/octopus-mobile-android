package com.octopustechnology.octopusapps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.octopustechnology.octopusapps.data.*
import kotlinx.coroutines.launch

@Composable
fun BudgetScreen(
    token: String,
    api: com.octopustechnology.octopusapps.network.BudgetApiService,
    onLogout: () -> Unit
) {
    var subscriptions by remember { mutableStateOf<List<Subscription>>(emptyList()) }
    var accounts by remember { mutableStateOf<List<Account>>(emptyList()) }
    var income by remember { mutableStateOf<List<Income>>(emptyList()) }
    var debts by remember { mutableStateOf<List<Debt>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val authHeader = "Bearer $token"
                subscriptions = api.getSubscriptions(authHeader)
                accounts = api.getAccounts(authHeader)
                income = api.getIncome(authHeader)
                debts = api.getDebts(authHeader)
            } catch (e: Exception) {
                errorMessage = "Error loading data: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Budget Tracker",
                style = MaterialTheme.typography.headlineMedium
            )
            TextButton(onClick = onLogout) {
                Text("Logout")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Subscriptions Section
                item {
                    SectionHeader("Subscriptions")
                }
                if (subscriptions.isEmpty()) {
                    item {
                        Text("No subscriptions yet", style = MaterialTheme.typography.bodyMedium)
                    }
                } else {
                    items(subscriptions) { subscription ->
                        SubscriptionCard(subscription)
                    }
                }

                // Accounts Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    SectionHeader("Accounts")
                }
                if (accounts.isEmpty()) {
                    item {
                        Text("No accounts yet", style = MaterialTheme.typography.bodyMedium)
                    }
                } else {
                    items(accounts) { account ->
                        AccountCard(account)
                    }
                }

                // Income Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    SectionHeader("Income")
                }
                if (income.isEmpty()) {
                    item {
                        Text("No income recorded", style = MaterialTheme.typography.bodyMedium)
                    }
                } else {
                    items(income) { inc ->
                        IncomeCard(inc)
                    }
                }

                // Debts Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    SectionHeader("Debts")
                }
                if (debts.isEmpty()) {
                    item {
                        Text("No debts", style = MaterialTheme.typography.bodyMedium)
                    }
                } else {
                    items(debts) { debt ->
                        DebtCard(debt)
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun SubscriptionCard(subscription: Subscription) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = subscription.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "$${subscription.amount} / ${subscription.frequency}",
                style = MaterialTheme.typography.bodyLarge
            )
            if (subscription.category != null) {
                Text(
                    text = "Category: ${subscription.category}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            if (subscription.notes != null) {
                Text(
                    text = subscription.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AccountCard(account: Account) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = account.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Balance: $${account.balance}",
                style = MaterialTheme.typography.bodyLarge
            )
            if (account.type != null) {
                Text(
                    text = "Type: ${account.type}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun IncomeCard(income: Income) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (income.source != null) {
                Text(
                    text = income.source,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = "$${income.amount} / ${income.frequency}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun DebtCard(debt: Debt) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = debt.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Amount: $${debt.amount}",
                style = MaterialTheme.typography.bodyLarge
            )
            if (debt.balance != null) {
                Text(
                    text = "Balance: $${debt.balance}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (debt.interest_rate != null) {
                Text(
                    text = "Interest Rate: ${debt.interest_rate}%",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
