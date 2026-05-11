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
import com.example.updatd_mahila_shakthi.ui.components.*
import com.example.updatd_mahila_shakthi.ui.theme.*
import com.example.updatd_mahila_shakthi.utils.DateUtils
import com.example.updatd_mahila_shakthi.viewmodel.LoanRepaymentViewModel

@Composable
fun LoanRepaymentScreen(loanId: Long, viewModel: LoanRepaymentViewModel, onNavigateBack: () -> Unit) {
    val activeLoans by viewModel.activeLoans.collectAsState()
    val members by viewModel.members.collectAsState()
    val selectedLoan by viewModel.selectedLoan.collectAsState()
    val repayments by viewModel.repayments.collectAsState()
    val message by viewModel.message.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedLoanId by remember { mutableStateOf(if (loanId > 0) loanId else 0L) }
    var amount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    LaunchedEffect(loanId) { if (loanId > 0) viewModel.loadLoan(loanId) }
    LaunchedEffect(message) { message?.let { snackbarHostState.showSnackbar(it); viewModel.clearMessage() } }

    Scaffold(
        topBar = { AppTopBar("Loan Repayment", canNavigateBack = true, onNavigateBack = onNavigateBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            if (selectedLoan != null) {
                val loan = selectedLoan!!
                val memberName = members.find { it.memberId == loan.memberId }?.name ?: "Unknown"
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(memberName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text("Loan: ₹%.2f | Remaining: ₹%.2f".format(loan.loanAmount, loan.remainingAmount), style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Repayment Amount (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes (Optional)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(12.dp))
                GradientButton(text = "Record Payment", onClick = {
                    amount.toDoubleOrNull()?.let { viewModel.recordRepayment(loan.loanId, it, notes); amount = ""; notes = "" }
                }, enabled = amount.toDoubleOrNull() != null && (amount.toDoubleOrNull() ?: 0.0) > 0)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Payment History", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                LazyColumn { items(repayments) { r ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = MaterialTheme.shapes.small) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("₹%.2f".format(r.amount), fontWeight = FontWeight.SemiBold, color = SuccessGreen)
                                if (r.notes.isNotBlank()) Text(r.notes, style = MaterialTheme.typography.bodySmall)
                            }
                            Text(DateUtils.formatDateShort(r.date), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }}
            } else {
                Text("Select a loan to record repayment", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn { items(activeLoans) { loan ->
                    val name = members.find { it.memberId == loan.memberId }?.name ?: "Unknown"
                    Card(onClick = { selectedLoanId = loan.loanId; viewModel.loadLoan(loan.loanId) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(name, fontWeight = FontWeight.SemiBold)
                                Text("₹%.2f remaining".format(loan.remainingAmount), style = MaterialTheme.typography.bodySmall)
                            }
                            Icon(Icons.Default.ChevronRight, null)
                        }
                    }
                }}
            }
        }
    }
}
