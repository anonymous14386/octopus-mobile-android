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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
    var income by remember { mutableStateOf<List<Income>>(emptyList()) }
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
    var debts by remember { mutableStateOf<List<Debt>>(emptyList()) }
    var editingDebt by remember { mutableStateOf<Debt?>(null) }
                accounts = api.getAccounts(authHeader)
                income = api.getIncome(authHeader)
                debts = api.getDebts(authHeader)
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

    // Calculate totals
    val totalDebt = debts.sumOf { it.balance ?: it.amount }
    val monthlyIncome = income.sumOf { inc ->
        when (inc.frequency.lowercase()) {
            "weekly" -> inc.amount * 4.33
            "biweekly" -> inc.amount * 2.166667
            else -> inc.amount
        }
    }
    val totalBalance = accounts.sumOf { it.balance }
    val subscriptionTotal = subscriptions.sumOf { sub ->
        when (sub.frequency.lowercase()) {
            "daily" -> sub.amount * 30
            "weekly" -> sub.amount * 4.33
            "yearly" -> sub.amount / 12
            else -> sub.amount
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("💰 Budget Tracker", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Logout", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF667EEA)
                )
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
                Button(onClick = { loadData() }, modifier = Modifier.padding(top = 16.dp)) {
                    Text("Retry")
                }
            }
        } else {
            // Responsive grid for summary cards (2x2)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF5F7FA)),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SummaryCard(
                                title = "💳 Total Debt",
                                amount = totalDebt,
                                color = Color(0xFFDC3545),
                                modifier = Modifier.weight(1f)
                            )
                            SummaryCard(
                                title = "💵 Income",
                                amount = monthlyIncome,
                                color = Color(0xFF28A745),
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SummaryCard(
                                title = "🏦 Accounts",
                                amount = totalBalance,
                                color = Color(0xFF667EEA),
                                modifier = Modifier.weight(1f)
                            )
                            SummaryCard(
                                title = "📊 Subs/mo",
                                amount = subscriptionTotal,
                                color = Color(0xFF667EEA),
                                subtitle = "${subscriptions.size} subscriptions",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Subscriptions Section
                item {
                    SectionHeader("📺 Subscriptions") {
                        showAddDialog = "subscription"
                    }
                }
                if (subscriptions.isEmpty()) {
                    item {
                        EmptyState("No subscriptions yet")
                    }
                } else {
                    items(subscriptions) { subscription ->
                        SubscriptionCard(subscription)
                    }
                }

                // Accounts Section
                item {
                    SectionHeader("🏦 Accounts") {
                        showAddDialog = "account"
                    }
                }
                if (accounts.isEmpty()) {
                    item {
                        EmptyState("No accounts yet")
                    }
                } else {
                    items(accounts) { account ->
                        AccountCard(account)
                    }
                }

                // Income Section
                item {
                    SectionHeader("💵 Income") {
                        showAddDialog = "income"
                    }
                }
                if (income.isEmpty()) {
                    item {
                        EmptyState("No income recorded")
                    }
                } else {
                    items(income) { inc ->
                        IncomeCard(inc)
                    }
                }

                // Debts Section
                item {
                    SectionHeader("💳 Debts") {
                        showAddDialog = "debt"
                    }
                }
                if (debts.isEmpty()) {
                    item {
                        EmptyState("No debts")
                    }
                } else {
                    items(debts) { debt ->
                        DebtCard(debt)
                    }
                        editingDebt = null
                        showAddDialog = "debt"
            }
        }
    }

    // Add Dialog
    showAddDialog?.let { type ->
        AddItemDialog(
            type = type,
            api = api,
                            editingDebt = debt
                            showAddDialog = "debt"
            token = token,
            onDismiss = { showAddDialog = null },
            onSuccess = {
                showAddDialog = null
                loadData()
            }
        )
    }
}

@Composable
fun SummaryCard(
    title: String,
    amount: Double,
    color: Color,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    val isDark = MaterialTheme.colorScheme.background == Color(0xFF222222)
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (isDark) Color.Black else Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = if (isDark) Color.Green else Color(0xFF888888),
                fontWeight = FontWeight.SemiBold
            )
            val valueText = when {
                title.contains("Weight", ignoreCase = true) -> String.format("%.2f lbs", amount)
                title.contains("Exercise", ignoreCase = true) -> String.format("%.0f min", amount)
                title.contains("Calories", ignoreCase = true) -> String.format("%.0f cal", amount)
                title.contains("Goals", ignoreCase = true) -> String.format("%.0f", amount)
                else -> "$${String.format("%.2f", amount)}"
            }
            Text(
                text = valueText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                modifier = Modifier.padding(top = 8.dp)
            )
            subtitle?.let {
                Text(
                    text = it,
                    fontSize = 11.sp,
                    color = if (isDark) Color.Green else Color(0xFF888888),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, onAdd: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            FloatingActionButton(
                onClick = onAdd,
                modifier = Modifier.size(40.dp),
                containerColor = Color(0xFF667EEA)
            ) {
                Icon(Icons.Default.Add, "Add", tint = Color.White)
            }
        }
    }
}

@Composable
fun EmptyState(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(32.dp),
            color = Color(0xFF888888),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun SubscriptionCard(subscription: Subscription) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subscription.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "$${String.format("%.2f", subscription.amount)} / ${subscription.frequency}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF667EEA),
                    modifier = Modifier.padding(top = 4.dp)
                )
                if (subscription.category != null) {
                    Text(
                        text = "Category: ${subscription.category}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF888888),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AccountCard(account: Account) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = account.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Balance: $${String.format("%.2f", account.balance)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF28A745),
                    modifier = Modifier.padding(top = 4.dp)
                )
                if (account.type != null) {
                    Text(
                        text = "Type: ${account.type}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF888888),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun IncomeCard(income: Income) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                if (income.source != null) {
                    Text(
                        text = income.source,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = "$${String.format("%.2f", income.amount)} / ${income.frequency}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF28A745),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun DebtCard(debt: Debt) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = debt.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Amount: $${String.format("%.2f", debt.amount)}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFDC3545),
                modifier = Modifier.padding(top = 4.dp)
            )
            if (debt.balance != null) {
                Text(
                    text = "Balance: $${String.format("%.2f", debt.balance)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF555555),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            if (debt.interest_rate != null) {
                Text(
                    text = "Interest Rate: ${debt.interest_rate}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF888888),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // Add this line
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
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {

                val isEdit = type == "debt" && debtToEdit != null
                Text(
                    text = if (isEdit) "Edit Debt" else "Add ${type.capitalize()}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (type) {
                    "subscription" -> {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Amount") },
                            modifier = Modifier.fillMaxWidth()
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
                                label = { Text("Frequency") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                listOf("daily", "weekly", "monthly", "yearly").forEach { freq ->
                                    DropdownMenuItem(
                                        text = { Text(freq.capitalize()) },
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
                            label = { Text("Account Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Balance") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    "income" -> {
                        OutlinedTextField(
                            value = source,
                            onValueChange = { source = it },
                            label = { Text("Source (Optional)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Amount") },
                            modifier = Modifier.fillMaxWidth()
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
                                label = { Text("Frequency") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                listOf("weekly", "biweekly", "monthly", "yearly").forEach { freq ->
                                    DropdownMenuItem(
                                        text = { Text(freq.capitalize()) },
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
                            label = { Text("Debt Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Amount") },
                            modifier = Modifier.fillMaxWidth()
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
                        enabled = !isLoading
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
                                                                    Subscription(name = name, amount = amountValue, frequency = "monthly")
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
                                                                    Income(source = source, amount = amountValue, frequency = "monthly")
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
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White
                            )
                        } else {
                            Text("Add")
                        }
                    }
                }
            }
        }
    }
}

fun String.capitalize() = replaceFirstChar { it.uppercase() }
