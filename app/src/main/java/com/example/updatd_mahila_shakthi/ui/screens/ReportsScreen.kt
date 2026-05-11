package com.example.updatd_mahila_shakthi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.updatd_mahila_shakthi.ui.components.*
import com.example.updatd_mahila_shakthi.utils.ShareUtils
import com.example.updatd_mahila_shakthi.viewmodel.ReportViewModel

@Composable
fun ReportsScreen(viewModel: ReportViewModel, onNavigateBack: () -> Unit) {
    val reportText by viewModel.reportText.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) { viewModel.generateReport() }

    Scaffold(topBar = {
        AppTopBar("Reports", canNavigateBack = true, onNavigateBack = onNavigateBack, actions = {
            if (reportText.isNotBlank()) {
                IconButton(onClick = { ShareUtils.shareReport(context, reportText) }) {
                    Icon(Icons.Default.Share, "Share")
                }
            }
        })
    }) { padding ->
        if (isLoading) { LoadingIndicator() }
        else {
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
                Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium) {
                    Text(reportText.ifBlank { "No data available. Add members and transactions first." },
                        modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { viewModel.generateReport() }, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.Refresh, null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Refresh")
                    }
                    Button(onClick = { ShareUtils.shareReport(context, reportText) }, modifier = Modifier.weight(1f), enabled = reportText.isNotBlank()) {
                        Icon(Icons.Default.Share, null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Share Report")
                    }
                }
            }
        }
    }
}
