package com.octopustechnology.octopusapps.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.octopustechnology.octopusapps.data.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    token: String,
    api: com.octopustechnology.octopusapps.network.BudgetApiService,
    onLogout: () -> Unit
) {
    var subscriptions by remember { mutableStateOf<List<Subscription>>(emptyList()) }
    var accounts by remember { mutableStateOf<List<Account>>(emptyList()) }
    var incomes by remember { mutableStateOf<List<Income>>(emptyList()) }
    var debts by remember { mutableStateOf<List<Debt>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val loadData = {
        scope.launch {
            isLoading = true
            try {
                val authHeader = "Bearer $token"
                subscriptions = api.getSubscriptions(authHeader).data
                accounts = api.getAccounts(authHeader).data
                incomes = api.getIncome(authHeader).data
                debts = api.getDebts(authHeader).data
                errorMessage = ""
            } catch (e: Exception) {
                errorMessage = "Error loading data: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadData()
    }

    val totalSubscriptions = subscriptions.sumOf { it.amount }
    val totalAccounts = accounts.sumOf { it.balance }
    val totalIncome = incomes.sumOf { it.amount }
    val totalDebts = debts.sumOf { it.amount }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("💰 Budget Tracker") },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Logout", color = Color(0xFF667EEA))
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF667EEA))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Summary Cards
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SummaryCard(
                            title = "📊 Total Income",
                            amount = totalIncome,
                            color = Color(0xFF28A745),
                            subtitle = "${incomes.size} sources",
                            modifier = Modifier.weight(1f)
                        )
                        SummaryCard(
                            title = "💳 Subscriptions",
                            amount = totalSubscriptions,
                            color = Color(0xFFFF6B6B),
                            subtitle = "${subscriptions.size} active",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SummaryCard(
                            title = "🏦 Accounts",
                            amount = totalAccounts,
                            color = Color(0xFF667EEA),
                            subtitle = "${accounts.size} accounts",
                            modifier = Modifier.weight(1f)
                        )
                        SummaryCard(
                            title = "📉 Total Debt",
                            amount = totalDebts,
                            color = Color(0xFFFF6B6B),
                            subtitle = "${debts.size} debts",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Subscriptions Section
                item { SectionHeader("Subscriptions") { showAddDialog = "subscription" } }
                items(subscriptions) { sub ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D2D))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(sub.name, fontWeight = FontWeight.Medium, color = Color(0xFF28A745))
                            Text("$${String.format("%.2f", sub.amount)}/${sub.frequency}", color = Color(0xFFFF6B6B))
                        }
                    }
                }

                // Accounts Section
                item { SectionHeader("Accounts") { showAddDialog = "account" } }
                items(accounts) { acc ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D2D))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(acc.name, fontWeight = FontWeight.Medium, color = Color(0xFF28A745))
                            Text("$${String.format("%.2f", acc.balance)}", color = Color(0xFF28A745))
                        }
                    }
                }

                // Income Section
                item { SectionHeader("Income") { showAddDialog = "income" } }
                items(incomes) { inc ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D2D))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(inc.source ?: "Income", fontWeight = FontWeight.Medium, color = Color(0xFF28A745))
                            Text("$${String.format("%.2f", inc.amount)}/${inc.frequency}", color = Color(0xFF28A745))
                        }
                    }
                }

                // Debts Section
                item { SectionHeader("Debts") { showAddDialog = "debt" } }
                items(debts) { debt ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D2D))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(debt.name, fontWeight = FontWeight.Medium, color = Color(0xFF28A745))
                            Text("$${String.format("%.2f", debt.amount)}", color = Color(0xFFDC3545))
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }

    // Add Dialog
    showAddDialog?.let { type ->
        AddItemDialog(
            type = type,
            api = api,
            token = token,
            onDismiss = { showAddDialog = null },
            onSuccess = {
                showAddDialog = null
                loadData()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemDialog(
    type: String,
    api: com.octopustechnology.octopusapps.network.BudgetApiService,
    token: String,
    debtToEdit: Debt? = null,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    var name by remember { mutableStateOf(debtToEdit?.name ?: "") }
    var amount by remember { mutableStateOf(debtToEdit?.amount?.toString() ?: "") }
    var frequency by remember { mutableStateOf("monthly") }
    var source by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                val isEdit = type == "debt" && debtToEdit != null
                Text(
                    text = if (isEdit) "Edit Debt" else "Add ${type.replaceFirstChar { it.uppercase() }}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6B6B)
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (type) {
                    "subscription" -> {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Name", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF667EEA),
                                unfocusedBorderColor = Color.Gray
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Amount", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF667EEA),
                                unfocusedBorderColor = Color.Gray
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        var expanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = frequency,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Frequency", color = Color.Gray) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color(0xFF667EEA),
                                    unfocusedBorderColor = Color.Gray
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                listOf("daily", "weekly", "monthly", "yearly").forEach { freq ->
                                    DropdownMenuItem(
                                        text = { Text(freq.replaceFirstChar { it.uppercase() }) },
                                        onClick = {
                                            frequency = freq
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    "account" -> {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Account Name", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF667EEA),
                                unfocusedBorderColor = Color.Gray
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Balance", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF667EEA),
                                unfocusedBorderColor = Color.Gray
                            )
                        )
                    }
                    "income" -> {
                        OutlinedTextField(
                            value = source,
                            onValueChange = { source = it },
                            label = { Text("Source (Optional)", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF667EEA),
                                unfocusedBorderColor = Color.Gray
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Amount", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF667EEA),
                                unfocusedBorderColor = Color.Gray
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        var expanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = frequency,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Frequency", color = Color.Gray) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color(0xFF667EEA),
                                    unfocusedBorderColor = Color.Gray
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                listOf("weekly", "biweekly", "monthly", "yearly").forEach { freq ->
                                    DropdownMenuItem(
                                        text = { Text(freq.replaceFirstChar { it.uppercase() }) },
                                        onClick = {
                                            frequency = freq
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    "debt" -> {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Debt Name", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF667EEA),
                                unfocusedBorderColor = Color.Gray
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Amount", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF667EEA),
                                unfocusedBorderColor = Color.Gray
                            )
                        )
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        enabled = !isLoading,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            scope.launch {
                                isLoading = true
                                errorMessage = ""
                                try {
                                    val authHeader = "Bearer $token"
                                    val amountValue = amount.toDoubleOrNull()
                                    if (amountValue == null) {
                                        errorMessage = "Invalid amount"
                                        isLoading = false
                                        return@launch
                                    }
                                    when (type) {
                                        "subscription" -> {
                                            api.addSubscription(
                                                authHeader,
                                                Subscription(name = name, amount = amountValue, frequency = frequency)
                                            )
                                        }
                                        "account" -> {
                                            api.addAccount(
                                                authHeader,
                                                Account(name = name, balance = amountValue)
                                            )
                                        }
                                        "income" -> {
                                            api.addIncome(
                                                authHeader,
                                                Income(source = source, amount = amountValue, frequency = frequency)
                                            )
                                        }
                                        "debt" -> {
                                            if (name.isBlank()) {
                                                errorMessage = "Name is required"
                                                isLoading = false
                                                return@launch
                                            }
                                            if (isEdit && debtToEdit != null && debtToEdit.id != null) {
                                                api.updateDebt(
                                                    authHeader,
                                                    debtToEdit.id,
                                                    Debt(
                                                        id = debtToEdit.id,
                                                        name = name,
                                                        amount = amountValue,
                                                        balance = debtToEdit.balance,
                                                        interest_rate = debtToEdit.interest_rate,
                                                        minimum_payment = debtToEdit.minimum_payment,
                                                        due_date = debtToEdit.due_date,
                                                        notes = debtToEdit.notes
                                                    )
                                                )
                                            } else {
                                                api.addDebt(
                                                    authHeader,
                                                    Debt(name = name, amount = amountValue)
                                                )
                                            }
                                        }
                                    }
                                    onSuccess()
                                } catch (e: Exception) {
                                    errorMessage = "Error: ${e.message}"
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF667EEA)
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White
                            )
                        } else {
                            Text(if (isEdit) "Save" else "Add")
                        }
                    }
                }
            }
        }
    }
}