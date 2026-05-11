package com.example.updatd_mahila_shakthi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.updatd_mahila_shakthi.ui.components.AppTopBar
import com.example.updatd_mahila_shakthi.ui.components.StatCard
import com.example.updatd_mahila_shakthi.ui.theme.*
import com.example.updatd_mahila_shakthi.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    val totalMembers by viewModel.totalMembers.collectAsState(initial = 0)
    val totalSavings by viewModel.totalSavings.collectAsState(initial = 0.0)
    val activeLoans by viewModel.activeLoans.collectAsState(initial = 0)
    val pendingPayments by viewModel.pendingPayments.collectAsState(initial = 0)

    Scaffold(
        topBar = {
            AppTopBar("Dashboard", actions = {
                IconButton(onClick = { onNavigate("profile") }) { Icon(Icons.Default.Person, "Profile") }
                IconButton(onClick = { onNavigate("settings") }) { Icon(Icons.Default.Settings, "Settings") }
                IconButton(onClick = onLogout) { Icon(Icons.Default.Logout, "Logout") }
            })
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            // Stats Grid
            Text("Overview", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Total Members", totalMembers.toString(), Icons.Default.Groups, listOf(PrimaryPurple, PrimaryPurpleDark), Modifier.weight(1f))
                StatCard("Total Savings", "₹${totalSavings.toInt()}", Icons.Default.Savings, listOf(SuccessGreen, Color(0xFF2E7D32)), Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Active Loans", activeLoans.toString(), Icons.Default.AccountBalance, listOf(TertiaryAmber, Color(0xFFE65100)), Modifier.weight(1f))
                StatCard("Pending", pendingPayments.toString(), Icons.Default.PendingActions, listOf(SecondaryPink, Color(0xFFC2185B)), Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Quick Actions", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))

            // Quick Actions Grid
            val actions = listOf(
                Triple("Add Member", Icons.Default.PersonAdd, "add_edit_member/-1"),
                Triple("Add Savings", Icons.Default.Savings, "savings"),
                Triple("Apply Loan", Icons.Default.RequestQuote, "loans"),
                Triple("Repayment", Icons.Default.Payment, "loan_repayment/0"),
                Triple("Reports", Icons.Default.Assessment, "reports"),
                Triple("Members", Icons.Default.Groups, "member_list")
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                actions.chunked(3).forEach { row ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        row.forEach { (label, icon, route) ->
                            QuickActionCard(label, icon, { onNavigate(route) }, Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionCard(label: String, icon: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier.height(100.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, label, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium, maxLines = 1)
        }
    }
}
