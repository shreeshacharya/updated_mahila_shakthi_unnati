package com.example.updatd_mahila_shakthi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.updatd_mahila_shakthi.ui.components.AppTopBar
import com.example.updatd_mahila_shakthi.ui.theme.*
import com.example.updatd_mahila_shakthi.viewmodel.AuthState

@Composable
fun ProfileScreen(authState: AuthState, onNavigateBack: () -> Unit) {
    val user = authState.currentUser
    Scaffold(topBar = { AppTopBar("Profile", canNavigateBack = true, onNavigateBack = onNavigateBack) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.fillMaxWidth().height(180.dp).background(Brush.verticalGradient(listOf(PrimaryPurple, SecondaryPink))),
                contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center) {
                        Text(user?.name?.take(1)?.uppercase() ?: "U", style = MaterialTheme.typography.displayLarge, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(user?.name ?: "User", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                }
            }
            Column(modifier = Modifier.padding(24.dp)) {
                ProfileItem(Icons.Default.Email, "Email", user?.email ?: "—")
                ProfileItem(Icons.Default.Phone, "Phone", user?.phone ?: "—")
                ProfileItem(Icons.Default.CalendarToday, "Joined", com.example.updatd_mahila_shakthi.utils.DateUtils.formatDate(user?.createdAt ?: 0))
            }
        }
    }
}

@Composable
private fun ProfileItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
    }
    HorizontalDivider()
}
