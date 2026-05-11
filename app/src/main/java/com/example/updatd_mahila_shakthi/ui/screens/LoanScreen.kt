package com.example.updatd_mahila_shakthi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.updatd_mahila_shakthi.data.model.LoanStatus
import com.example.updatd_mahila_shakthi.data.model.Member
import com.example.updatd_mahila_shakthi.ui.components.*
import com.example.updatd_mahila_shakthi.ui.theme.*
import com.example.updatd_mahila_shakthi.utils.DateUtils
import com.example.updatd_mahila_shakthi.utils.InterestCalculator
import com.example.updatd_mahila_shakthi.viewmodel.LoanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanScreen(viewModel: LoanViewModel, onRepayment: (Long) -> Unit, onNavigateBack: () -> Unit) {
    val allLoans by viewModel.allLoans.collectAsState()
    val members by viewModel.members.collectAsState()
    val totalSavings by viewModel.totalSavings.collectAsState()
    val message by viewModel.message.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showApplyDialog by remember { mutableStateOf(false) }

    LaunchedEffect(message) { message?.let { snackbarHostState.showSnackbar(it); viewModel.clearMessage() } }

    Scaffold(
        topBar = { AppTopBar("Loans", canNavigateBack = true, onNavigateBack = onNavigateBack) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showApplyDialog = true }, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.Add, "Apply Loan", tint = MaterialTheme.colorScheme.onPrimary)
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (allLoans.isEmpty()) {
                EmptyStateView("No loans yet.")
            } else {
                LazyColumn {
                    items(allLoans, key = { it.loanId }) { loan ->
                        val memberName = members.find { it.memberId == loan.memberId }?.name ?: "Unknown"
                        val statusColor = when (loan.status) {
                            LoanStatus.ACTIVE -> TertiaryAmber
                            LoanStatus.CLOSED -> SuccessGreen
                            LoanStatus.DEFAULTED -> ErrorRed
                            else -> InfoBlue
                        }
                        Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp), shape = MaterialTheme.shapes.medium) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(memberName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                        Text("₹%.2f @ %.1f%%".format(loan.loanAmount, loan.interestRate), style = MaterialTheme.typography.bodyMedium)
                                    }
                                    AssistChip(onClick = {}, label = { Text(loan.status.name, style = MaterialTheme.typography.labelSmall) },
                                        colors = AssistChipDefaults.assistChipColors(containerColor = statusColor.copy(alpha = 0.15f)))
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Remaining", style = MaterialTheme.typography.bodySmall)
                                        Text("₹%.2f".format(loan.remainingAmount), fontWeight = FontWeight.SemiBold, color = ErrorRed)
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Paid", style = MaterialTheme.typography.bodySmall)
                                        Text("₹%.2f".format(loan.paidAmount), fontWeight = FontWeight.SemiBold, color = SuccessGreen)
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Due", style = MaterialTheme.typography.bodySmall)
                                        Text(DateUtils.formatDateShort(loan.dueDate), fontWeight = FontWeight.SemiBold)
                                    }
                                }
                                if (loan.status != LoanStatus.CLOSED) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedButton(onClick = { onRepayment(loan.loanId) }, modifier = Modifier.fillMaxWidth()) {
                                        Icon(Icons.Default.Payment, null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Record Repayment")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showApplyDialog) {
        ApplyLoanDialog(members, totalSavings, onDismiss = { showApplyDialog = false },
            onApply = { memberId, amount, rate, months -> viewModel.applyLoan(memberId, amount, rate, months); showApplyDialog = false })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ApplyLoanDialog(
    members: List<Member>,
    totalSavings: Double,
    onDismiss: () -> Unit,
    onApply: (Long, Double, Double, Int) -> Unit
) {
    var selectedMemberId by remember { mutableStateOf<Long?>(null) }
    var searchText by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("12") }
    var months by remember { mutableStateOf("12") }
    var expanded by remember { mutableStateOf(false) }

    val filteredMembers = members.filter { it.name.contains(searchText, ignoreCase = true) }

    val interest = InterestCalculator.calculateSimpleInterest(
        amount.toDoubleOrNull() ?: 0.0, rate.toDoubleOrNull() ?: 0.0, months.toIntOrNull() ?: 0)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Apply for Loan") },
        text = {
            Column {
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { 
                            searchText = it
                            selectedMemberId = null
                            expanded = true
                        },
                        label = { Text("Search Member") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryEditable)
                    )
                    if (filteredMembers.isNotEmpty()) {
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            filteredMembers.forEach { m ->
                                DropdownMenuItem(
                                    text = { Text(m.name) },
                                    onClick = { 
                                        selectedMemberId = m.memberId
                                        searchText = m.name
                                        expanded = false 
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = amount, onValueChange = { amount = it },
                    label = { Text("Loan Amount (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = rate, onValueChange = { rate = it },
                        label = { Text("Rate %") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f))
                    OutlinedTextField(value = months, onValueChange = { months = it },
                        label = { Text("Months") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Interest: ₹%.2f | Total: ₹%.2f".format(interest, (amount.toDoubleOrNull() ?: 0.0) + interest),
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                val loanAmt = amount.toDoubleOrNull() ?: 0.0
                Text(
                    text = if (loanAmt >= totalSavings && totalSavings > 0) "⚠️ Must be below group savings (₹%.0f)".format(totalSavings)
                           else "📊 Max allowed: ₹%.0f (group savings)".format(totalSavings),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (loanAmt >= totalSavings && totalSavings > 0) com.example.updatd_mahila_shakthi.ui.theme.ErrorRed
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amt = amount.toDoubleOrNull()
                    val rt = rate.toDoubleOrNull()
                    val mths = months.toIntOrNull()
                    if (selectedMemberId != null && amt != null && rt != null && mths != null) {
                        onApply(selectedMemberId!!, amt, rt, mths)
                    }
                },
                enabled = selectedMemberId != null && amount.toDoubleOrNull() != null && rate.toDoubleOrNull() != null && months.toIntOrNull() != null
            ) { Text("Apply") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
