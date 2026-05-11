package com.example.updatd_mahila_shakthi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.updatd_mahila_shakthi.ui.components.AppTopBar
import com.example.updatd_mahila_shakthi.ui.components.FormTextField
import com.example.updatd_mahila_shakthi.ui.components.GradientButton
import com.example.updatd_mahila_shakthi.viewmodel.AuthState

@Composable
fun SignupScreen(
    authState: AuthState,
    onRegister: (String, String, String, String, String) -> Unit,
    onNavigateBack: () -> Unit,
    onClearError: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(topBar = { AppTopBar("Create Account", canNavigateBack = true, onNavigateBack = onNavigateBack) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp).verticalScroll(rememberScrollState())
        ) {
            Text("Join Us", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Create your account to get started", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(24.dp))

            FormTextField(value = name, onValueChange = { name = it; onClearError() }, label = "Full Name",
                leadingIcon = { Icon(Icons.Default.Person, null) })
            Spacer(modifier = Modifier.height(12.dp))

            FormTextField(value = email, onValueChange = { email = it; onClearError() }, label = "Email",
                leadingIcon = { Icon(Icons.Default.Email, null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
            Spacer(modifier = Modifier.height(12.dp))

            FormTextField(value = phone, onValueChange = { 
                if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                    phone = it; onClearError()
                }
            }, label = "Phone Number",
                leadingIcon = { Icon(Icons.Default.Phone, null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
            Spacer(modifier = Modifier.height(12.dp))

            FormTextField(value = password, onValueChange = { password = it; onClearError() }, label = "Password",
                leadingIcon = { Icon(Icons.Default.Lock, null) },
                trailingIcon = { IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null)
                }},
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
            Spacer(modifier = Modifier.height(12.dp))

            FormTextField(value = confirmPassword, onValueChange = { confirmPassword = it; onClearError() }, label = "Confirm Password",
                leadingIcon = { Icon(Icons.Default.Lock, null) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))

            authState.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
            GradientButton(
                text = if (authState.isLoading) "Registering..." else "Register",
                onClick = { onRegister(name, email, phone, password, confirmPassword) },
                enabled = !authState.isLoading && phone.length == 10
            )
        }
    }
}
