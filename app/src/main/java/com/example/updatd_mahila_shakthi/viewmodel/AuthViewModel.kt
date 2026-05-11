package com.example.updatd_mahila_shakthi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.updatd_mahila_shakthi.MahilaShaktiApp
import com.example.updatd_mahila_shakthi.data.model.User
import com.example.updatd_mahila_shakthi.utils.PasswordUtils
import com.example.updatd_mahila_shakthi.utils.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val currentUser: User? = null,
    val errorMessage: String? = null
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = (application as MahilaShaktiApp).userRepository

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        if (!Validators.isValidEmail(email)) {
            _authState.value = _authState.value.copy(errorMessage = "Invalid email address")
            return
        }
        if (!Validators.isValidPassword(password)) {
            _authState.value = _authState.value.copy(errorMessage = "Password must be at least 6 characters")
            return
        }
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, errorMessage = null)
            try {
                val hash = PasswordUtils.hashPassword(password)
                val user = userRepository.login(email.trim(), hash)
                if (user != null) {
                    _authState.value = AuthState(isLoggedIn = true, currentUser = user)
                } else {
                    _authState.value = _authState.value.copy(isLoading = false, errorMessage = "Invalid email or password")
                }
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(isLoading = false, errorMessage = "Login failed: ${e.message}")
            }
        }
    }

    fun register(name: String, email: String, phone: String, password: String, confirmPassword: String) {
        if (!Validators.isNotEmpty(name)) { _authState.value = _authState.value.copy(errorMessage = "Name is required"); return }
        if (!Validators.isValidEmail(email)) { _authState.value = _authState.value.copy(errorMessage = "Invalid email address"); return }
        if (!Validators.isValidPhone(phone)) { _authState.value = _authState.value.copy(errorMessage = "Phone must be 10 digits"); return }
        if (!Validators.isValidPassword(password)) { _authState.value = _authState.value.copy(errorMessage = "Password must be at least 6 characters"); return }
        if (!Validators.passwordsMatch(password, confirmPassword)) { _authState.value = _authState.value.copy(errorMessage = "Passwords do not match"); return }

        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, errorMessage = null)
            try {
                if (userRepository.emailExists(email.trim())) {
                    _authState.value = _authState.value.copy(isLoading = false, errorMessage = "Email already registered")
                    return@launch
                }
                val user = User(name = name.trim(), email = email.trim(), phone = phone.trim(), passwordHash = PasswordUtils.hashPassword(password))
                val id = userRepository.register(user)
                _authState.value = AuthState(isLoggedIn = true, currentUser = user.copy(id = id))
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(isLoading = false, errorMessage = "Registration failed: ${e.message}")
            }
        }
    }

    fun clearError() { _authState.value = _authState.value.copy(errorMessage = null) }
    fun logout() { _authState.value = AuthState() }
}
