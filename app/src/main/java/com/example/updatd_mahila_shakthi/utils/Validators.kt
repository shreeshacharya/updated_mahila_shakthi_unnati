package com.example.updatd_mahila_shakthi.utils

object Validators {
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPhone(phone: String): Boolean {
        return phone.length == 10 && phone.all { it.isDigit() }
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun isNotEmpty(value: String): Boolean {
        return value.isNotBlank()
    }

    fun passwordsMatch(password: String, confirm: String): Boolean {
        return password == confirm
    }

    fun isValidAmount(amount: String): Boolean {
        val value = amount.toDoubleOrNull()
        return value != null && value > 0
    }

    fun isValidRate(rate: String): Boolean {
        val value = rate.toDoubleOrNull()
        return value != null && value in 0.0..100.0
    }
}
