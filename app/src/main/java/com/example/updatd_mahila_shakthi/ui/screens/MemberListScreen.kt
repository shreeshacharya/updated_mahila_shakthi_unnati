package com.example.updatd_mahila_shakthi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.updatd_mahila_shakthi.ui.components.*
import com.example.updatd_mahila_shakthi.ui.theme.PrimaryPurple
import com.example.updatd_mahila_shakthi.viewmodel.MemberViewModel

enum class MemberFilter { ALL, PENDING_WEEKLY, ACTIVE_LOANS }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberListScreen(
    viewModel: MemberViewModel,
    onAddMember: () -> Unit,
    onMemberClick: (Long) -> Unit,
    onNavigateBack: () -> Unit
) {
    val memberStats by viewModel.memberStats.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val message by viewModel.operationMessage.collectAsState()
    var memberToDelete by remember { mutableStateOf<com.example.updatd_mahila_shakthi.data.model.Member?>(null) }
    
    var selectedFilter by remember { mutableStateOf(MemberFilter.ALL) }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(message) { message?.let { snackbarHostState.showSnackbar(it); viewModel.clearMessage() } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Members", color = PrimaryPurple, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Weekly Target Chip
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(PrimaryPurple.copy(alpha = 0.2f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Weekly Target: ₹150",
                            style = MaterialTheme.typography.labelMedium,
                            color = PrimaryPurple,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddMember, containerColor = PrimaryPurple) {
                Icon(Icons.Default.Add, "Add Member", tint = MaterialTheme.colorScheme.onPrimary)
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = searchQuery, onValueChange = { viewModel.onSearchQueryChange(it) },
                placeholder = { Text("Search members...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                shape = MaterialTheme.shapes.small, singleLine = true
            )
            
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == MemberFilter.ALL,
                    onClick = { selectedFilter = MemberFilter.ALL },
                    label = { Text("All") }
                )
                FilterChip(
                    selected = selectedFilter == MemberFilter.PENDING_WEEKLY,
                    onClick = { selectedFilter = MemberFilter.PENDING_WEEKLY },
                    label = { Text("Pending Weekly") }
                )
                FilterChip(
                    selected = selectedFilter == MemberFilter.ACTIVE_LOANS,
                    onClick = { selectedFilter = MemberFilter.ACTIVE_LOANS },
                    label = { Text("Active Loans") }
                )
            }

            val filteredStats = memberStats.filter { stat ->
                when (selectedFilter) {
                    MemberFilter.ALL -> true
                    MemberFilter.PENDING_WEEKLY -> !stat.paidThisWeek
                    MemberFilter.ACTIVE_LOANS -> stat.hasActiveLoan
                }
            }

            if (filteredStats.isEmpty()) {
                EmptyStateView(if (memberStats.isEmpty()) "No members yet. Tap + to add." else "No members match the selected filter.")
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredStats, key = { it.member.memberId }) { stat ->
                        MemberCard(
                            memberStats = stat,
                            onClick = { onMemberClick(stat.member.memberId) },
                            onDelete = { memberToDelete = stat.member }
                        )
                    }
                }
            }
        }
    }

    memberToDelete?.let { member ->
        ConfirmDialog("Delete Member", "Are you sure you want to delete ${member.name}?",
            onConfirm = { viewModel.deleteMember(member); memberToDelete = null },
            onDismiss = { memberToDelete = null })
    }
}
