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
import com.example.updatd_mahila_shakthi.data.model.PaymentStatus
import com.example.updatd_mahila_shakthi.ui.components.*
import com.example.updatd_mahila_shakthi.ui.theme.ErrorRed
import com.example.updatd_mahila_shakthi.ui.theme.SuccessGreen
import com.example.updatd_mahila_shakthi.ui.theme.WarningOrange
import com.example.updatd_mahila_shakthi.utils.DateUtils
import com.example.updatd_mahila_shakthi.viewmodel.SavingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsScreen(viewModel: SavingsViewModel, onNavigateBack: () -> Unit) {
    val members by viewModel.members.collectAsState()
    val totalSavings by viewModel.totalSavings.collectAsState()
    val allSavings by viewModel.allSavings.collectAsState()
    val message by viewModel.message.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(message) { message?.let { snackbarHostState.showSnackbar(it); viewModel.clearMessage() } }

    Scaffold(
        topBar = { AppTopBar("Savings", canNavigateBack = true, onNavigateBack = onNavigateBack) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.Add, "Add Savings", tint = MaterialTheme.colorScheme.onPrimary)
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Total savings header
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Savings, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Group Total Savings", style = MaterialTheme.typography.bodyMedium)
                        Text("₹%.2f".format(totalSavings), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            // Weekly ₹150 reminder
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = WarningOrange.copy(alpha = 0.1f))
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("⚡", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Weekly contribution: ₹150 per member", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = WarningOrange)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (allSavings.isEmpty()) {
                EmptyStateView("No savings entries yet.")
            } else {
                LazyColumn { items(allSavings, key = { it.savingsId }) { entry ->
                    val memberName = members.find { it.memberId == entry.memberId }?.name ?: "Unknown"
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp), shape = MaterialTheme.shapes.small) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            // Payment status signal icon
                            Icon(
                                imageVector = if (entry.status == PaymentStatus.PAID) Icons.Default.CheckCircle else Icons.Default.HourglassBottom,
                                contentDescription = entry.status.name,
                                tint = if (entry.status == PaymentStatus.PAID) SuccessGreen else ErrorRed,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(memberName, fontWeight = FontWeight.SemiBold)
                                Text("₹%.0f • Week ${entry.week} • ${DateUtils.formatDateShort(entry.date)}".format(entry.amount),
                                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            FilterChip(
                                selected = entry.status == PaymentStatus.PAID,
                                onClick = { viewModel.updateSavingsStatus(entry, if (entry.status == PaymentStatus.PAID) PaymentStatus.PENDING else PaymentStatus.PAID) },
                                label = { Text(if (entry.status == PaymentStatus.PAID) "✅ Paid" else "⏳ Pending") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SuccessGreen.copy(alpha = 0.15f),
                                    containerColor = ErrorRed.copy(alpha = 0.1f)
                                )
                            )
                        }
                    }
                }}
            }
        }
    }

    if (showAddDialog) {
        AddSavingsDialog(members, onDismiss = { showAddDialog = false }, onAdd = { memberId, amount, status -> viewModel.addSavingsEntry(memberId, amount, status); showAddDialog = false })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddSavingsDialog(
    members: List<com.example.updatd_mahila_shakthi.data.model.Member>,
    onDismiss: () -> Unit,
    onAdd: (Long, Double, PaymentStatus) -> Unit
) {
    var selectedMemberId by remember { mutableStateOf<Long?>(null) }
    var searchText by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("150") }  // Default to ₹150
    var isPaid by remember { mutableStateOf(true) }
    var expanded by remember { mutableStateOf(false) }

    val filteredMembers = members.filter { it.name.contains(searchText, ignoreCase = true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Weekly Savings") },
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
                            filteredMembers.forEach { member ->
                                DropdownMenuItem(
                                    text = { Text(member.name) }, 
                                    onClick = { 
                                        selectedMemberId = member.memberId
                                        searchText = member.name
                                        expanded = false 
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = amount, onValueChange = { amount = it },
                    label = { Text("Amount (₹)") },
                    supportingText = { Text("⚡ Weekly: ₹150", fontWeight = FontWeight.Medium, color = WarningOrange) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isPaid, onCheckedChange = { isPaid = it })
                    Text(if (isPaid) "✅ Paid" else "⏳ Pending", fontWeight = FontWeight.Medium)
                }
            }
        },
        confirmButton = { 
            TextButton(
                onClick = { 
                    val amt = amount.toDoubleOrNull()
                    if (selectedMemberId != null && amt != null) {
                        onAdd(selectedMemberId!!, amt, if (isPaid) PaymentStatus.PAID else PaymentStatus.PENDING) 
                    }
                },
                enabled = selectedMemberId != null && amount.toDoubleOrNull() != null
            ) { Text("Add") } 
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
