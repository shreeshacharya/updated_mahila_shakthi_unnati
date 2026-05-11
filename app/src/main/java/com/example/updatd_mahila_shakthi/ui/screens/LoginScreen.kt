package com.example.updatd_mahila_shakthi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.updatd_mahila_shakthi.ui.components.FormTextField
import com.example.updatd_mahila_shakthi.ui.components.GradientButton
import com.example.updatd_mahila_shakthi.ui.theme.*
import com.example.updatd_mahila_shakthi.viewmodel.AuthState

@Composable
fun LoginScreen(
    authState: AuthState,
    onLogin: (String, String) -> Unit,
    onNavigateToSignup: () -> Unit,
    onClearError: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier.fillMaxWidth().height(240.dp)
                .background(Brush.verticalGradient(listOf(PrimaryPurple, SecondaryPink))),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🪷", fontSize = 48.sp)
                Text("Mahila-Shakti Unnati", style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Text("Empowering Women Through Financial Unity", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
            }
        }

        Column(modifier = Modifier.padding(24.dp)) {
            Text("Welcome Back", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Sign in to continue", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(24.dp))

            FormTextField(
                value = email, onValueChange = { email = it; onClearError() }, label = "Email",
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(12.dp))

            FormTextField(
                value = password, onValueChange = { password = it; onClearError() }, label = "Password",
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, "Toggle password")
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
                Text("Remember Me", style = MaterialTheme.typography.bodyMedium)
            }

            authState.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(bottom = 8.dp))
            }

            GradientButton(
                text = if (authState.isLoading) "Signing in..." else "Login",
                onClick = { onLogin(email, password) },
                enabled = !authState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Don't have an account? ", style = MaterialTheme.typography.bodyMedium)
                TextButton(onClick = onNavigateToSignup) {
                    Text("Create Account", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
