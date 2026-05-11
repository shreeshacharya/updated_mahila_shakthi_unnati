package com.example.updatd_mahila_shakthi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.updatd_mahila_shakthi.MahilaShaktiApp
import com.example.updatd_mahila_shakthi.data.model.Member
import com.example.updatd_mahila_shakthi.data.model.PaymentStatus
import com.example.updatd_mahila_shakthi.data.model.Savings
import com.example.updatd_mahila_shakthi.utils.DateUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SavingsViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as MahilaShaktiApp

    val members: StateFlow<List<Member>> = app.memberRepository.getAllMembers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalSavings: StateFlow<Double> = app.savingsRepository.getTotalSavings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val allSavings: StateFlow<List<Savings>> = app.savingsRepository.getAllSavings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun addSavingsEntry(memberId: Long, amount: Double, status: PaymentStatus) {
        viewModelScope.launch {
            try {
                app.savingsRepository.insert(
                    Savings(
                        memberId = memberId, amount = amount, status = status,
                        week = DateUtils.getCurrentWeek(), month = DateUtils.getCurrentMonth(), year = DateUtils.getCurrentYear()
                    )
                )
                _message.value = "Savings entry added"
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }

    fun updateSavingsStatus(savings: Savings, newStatus: PaymentStatus) {
        viewModelScope.launch {
            app.savingsRepository.update(savings.copy(status = newStatus))
            _message.value = "Payment status updated"
        }
    }

    fun clearMessage() { _message.value = null }
}
