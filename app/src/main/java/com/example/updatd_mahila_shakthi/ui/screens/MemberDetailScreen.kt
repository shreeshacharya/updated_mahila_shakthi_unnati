package com.example.updatd_mahila_shakthi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.updatd_mahila_shakthi.data.model.PaymentStatus
import com.example.updatd_mahila_shakthi.ui.components.ConfirmDialog
import com.example.updatd_mahila_shakthi.ui.theme.ErrorRed
import com.example.updatd_mahila_shakthi.ui.theme.PrimaryPurple
import com.example.updatd_mahila_shakthi.ui.theme.SecondaryPink
import com.example.updatd_mahila_shakthi.ui.theme.WarningOrange
import com.example.updatd_mahila_shakthi.utils.ShareUtils
import com.example.updatd_mahila_shakthi.viewmodel.MemberDetailViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDetailScreen(
    memberId: Long,
    viewModel: MemberDetailViewModel,
    onNavigateToRepayment: (Long) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val member by viewModel.member.collectAsState()
    val totalSavings by viewModel.totalSavings.collectAsState()
    val groupTotalSavings by viewModel.groupTotalSavings.collectAsState()
    val unpaidLoans by viewModel.unpaidLoansSum.collectAsState()
    val activeLoans by viewModel.activeLoans.collectAsState()
    val isPendingThisWeek by viewModel.isPendingThisWeek.collectAsState()
    val message by viewModel.message.collectAsState()

    var showAddSavingsDialog by remember { mutableStateOf(false) }
    var showRequestLoanDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(memberId) { viewModel.loadMember(memberId) }
    LaunchedEffect(message) { message?.let { snackbarHostState.showSnackbar(it); viewModel.clearMessage() } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(member?.name ?: "", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "Back", tint = Color.White) }
                },
                actions = {
                    IconButton(onClick = {
                        member?.let {
                            val summary = """
                                📄 Member Summary
                                Name: ${it.name}
                                Phone: ${it.phone}
                                
                                💰 Total Saved: ₹${totalSavings.toInt()}
                                ⚠️ Unpaid Loans: ₹${unpaidLoans.toInt()}
                                ⏳ Pending Weekly: ${if (isPendingThisWeek) "Yes (₹150)" else "No"}
                                
                                Sent from Mahila-Shakti Unnati App
                            """.trimIndent()
                            ShareUtils.shareReport(context, summary)
                        }
                    }) {
                        Icon(Icons.Default.Share, "Share", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryPurple)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFF1E1E1E) // Dark background per mockup
    ) { padding ->
        if (member == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = SecondaryPink)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Profile Avatar
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.DarkGray)
                            .border(2.dp, SecondaryPink, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (member!!.profileImagePath.isNotBlank()) {
                            AsyncImage(
                                model = ImageRequest.Builder(context).data(File(member!!.profileImagePath)).crossfade(true).build(),
                                contentDescription = "Profile Photo",
                                modifier = Modifier.fillMaxSize().clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = member!!.name.take(1).uppercase(),
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(member!!.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(member!!.phone, style = MaterialTheme.typography.bodyMedium, color = Color.Cyan)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Cards Row
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF3A3A42)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Total Savings", style = MaterialTheme.typography.bodyMedium, color = Color.LightGray)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("₹%.2f".format(totalSavings), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                        
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF7A2021)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Unpaid Loans", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("₹%.2f".format(unpaidLoans), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Action Buttons Row
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Button(
                            onClick = { showAddSavingsDialog = true },
                            modifier = Modifier.weight(1f).height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SecondaryPink),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Text("Add Savings", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        
                        Button(
                            onClick = { showRequestLoanDialog = true },
                            modifier = Modifier.weight(1f).height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SecondaryPink),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Text("Request Loan", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Active Loans Header
                    Text(
                        "Active Loans",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                
                if (activeLoans.isEmpty()) {
                    item {
                        Text("No active loans", color = Color.Gray, modifier = Modifier.padding(16.dp))
                    }
                } else {
                    items(activeLoans) { loan ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF3A3A42)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "Principal: ₹${loan.loanAmount} @ ${loan.interestRate}% for ${com.example.updatd_mahila_shakthi.utils.DateUtils.getMonthsBetween(System.currentTimeMillis(), loan.dueDate)}m",
                                    color = Color.LightGray,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Remaining Balance: ₹%.2f".format(loan.remainingAmount),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { onNavigateToRepayment(loan.loanId) },
                                    colors = ButtonDefaults.buttonColors(containerColor = SecondaryPink),
                                    shape = RoundedCornerShape(25.dp)
                                ) {
                                    Text("Repay Installment", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddSavingsDialog) {
        var amount by remember { mutableStateOf("150") }
        AlertDialog(
            onDismissRequest = { showAddSavingsDialog = false },
            title = { Text("Add Savings") },
            text = {
                OutlinedTextField(
                    value = amount, onValueChange = { amount = it },
                    label = { Text("Amount (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    amount.toDoubleOrNull()?.let { viewModel.addSavingsEntry(it, PaymentStatus.PAID) }
                    showAddSavingsDialog = false
                }) { Text("Add") }
            },
            dismissButton = { TextButton(onClick = { showAddSavingsDialog = false }) { Text("Cancel") } }
        )
    }

    if (showRequestLoanDialog) {
        var amount by remember { mutableStateOf("") }
        var rate by remember { mutableStateOf("12") }
        var months by remember { mutableStateOf("12") }
        
        AlertDialog(
            onDismissRequest = { showRequestLoanDialog = false },
            title = { Text("Request Loan") },
            text = {
                Column {
                    OutlinedTextField(
                        value = amount, onValueChange = { amount = it },
                        label = { Text("Loan Amount (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = rate, onValueChange = { rate = it },
                            label = { Text("Rate %") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = months, onValueChange = { months = it },
                            label = { Text("Months") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    val loanAmt = amount.toDoubleOrNull() ?: 0.0
                    Text(
                        text = if (loanAmt >= groupTotalSavings && groupTotalSavings > 0) "⚠️ Must be below group savings (₹%.0f)".format(groupTotalSavings)
                               else "📊 Max allowed: ₹%.0f (group savings)".format(groupTotalSavings),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (loanAmt >= groupTotalSavings && groupTotalSavings > 0) ErrorRed else Color.DarkGray
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.requestLoan(
                        amount.toDoubleOrNull() ?: 0.0,
                        rate.toDoubleOrNull() ?: 0.0,
                        months.toIntOrNull() ?: 12
                    )
                    showRequestLoanDialog = false
                }) { Text("Request") }
            },
            dismissButton = { TextButton(onClick = { showRequestLoanDialog = false }) { Text("Cancel") } }
        )
    }
}
