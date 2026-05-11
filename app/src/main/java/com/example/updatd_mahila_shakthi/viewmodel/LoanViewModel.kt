package com.example.updatd_mahila_shakthi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.updatd_mahila_shakthi.MahilaShaktiApp
import com.example.updatd_mahila_shakthi.data.model.Loan
import com.example.updatd_mahila_shakthi.data.model.LoanStatus
import com.example.updatd_mahila_shakthi.data.model.Member
import com.example.updatd_mahila_shakthi.utils.DateUtils
import com.example.updatd_mahila_shakthi.utils.InterestCalculator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoanViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as MahilaShaktiApp

    val allLoans: StateFlow<List<Loan>> = app.loanRepository.getAllLoans()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val members: StateFlow<List<Member>> = app.memberRepository.getAllMembers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Total group savings — used to cap the maximum loan amount */
    val totalSavings: StateFlow<Double> = app.savingsRepository.getTotalSavings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun applyLoan(memberId: Long, amount: Double, rate: Double, durationMonths: Int) {
        viewModelScope.launch {
            try {
                // Business rule 1: No new loan if previous loan is unpaid
                if (app.loanRepository.hasUnpaidLoan(memberId)) {
                    _message.value = "⚠️ Existing unpaid loan detected. Repay the previous loan first."
                    return@launch
                }

                // Business rule 2: Loan amount must be below total group savings
                val currentTotalSavings = app.savingsRepository.getAllSavingsList()
                    .filter { it.status == com.example.updatd_mahila_shakthi.data.model.PaymentStatus.PAID }
                    .sumOf { it.amount }

                if (amount >= currentTotalSavings) {
                    _message.value = "⚠️ Loan amount (₹%.0f) must be less than total savings (₹%.0f)".format(amount, currentTotalSavings)
                    return@launch
                }

                if (amount <= 0) {
                    _message.value = "⚠️ Please enter a valid loan amount"
                    return@launch
                }

                val interest = InterestCalculator.calculateSimpleInterest(amount, rate, durationMonths)
                val totalRepayment = amount + interest
                val dueDate = DateUtils.addMonths(System.currentTimeMillis(), durationMonths)
                app.loanRepository.insertLoan(
                    Loan(memberId = memberId, loanAmount = amount, interestRate = rate, dueDate = dueDate, remainingAmount = totalRepayment, status = LoanStatus.ACTIVE)
                )
                _message.value = "✅ Loan of ₹%.0f approved (Interest: ₹%.0f, Total repay: ₹%.0f)".format(amount, interest, totalRepayment)
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }

    fun clearMessage() { _message.value = null }
}
