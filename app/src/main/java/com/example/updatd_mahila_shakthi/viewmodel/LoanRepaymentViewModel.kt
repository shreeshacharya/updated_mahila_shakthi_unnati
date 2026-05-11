package com.example.updatd_mahila_shakthi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.updatd_mahila_shakthi.MahilaShaktiApp
import com.example.updatd_mahila_shakthi.data.model.Loan
import com.example.updatd_mahila_shakthi.data.model.LoanRepayment
import com.example.updatd_mahila_shakthi.data.model.LoanStatus
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoanRepaymentViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as MahilaShaktiApp

    val activeLoans: StateFlow<List<Loan>> = app.loanRepository.getActiveLoans()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val members = app.memberRepository.getAllMembers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedLoan = MutableStateFlow<Loan?>(null)
    val selectedLoan: StateFlow<Loan?> = _selectedLoan

    private val _repayments = MutableStateFlow<List<LoanRepayment>>(emptyList())
    val repayments: StateFlow<List<LoanRepayment>> = _repayments

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun loadLoan(loanId: Long) {
        viewModelScope.launch {
            _selectedLoan.value = app.loanRepository.getLoanById(loanId)
            app.loanRepository.getRepaymentsByLoan(loanId).collect { _repayments.value = it }
        }
    }

    fun recordRepayment(loanId: Long, amount: Double, notes: String = "") {
        viewModelScope.launch {
            try {
                val loan = app.loanRepository.getLoanById(loanId)
                if (loan == null) { _message.value = "Loan not found"; return@launch }
                if (amount > loan.remainingAmount) { _message.value = "Amount exceeds remaining balance"; return@launch }

                app.loanRepository.insertRepayment(LoanRepayment(loanId = loanId, amount = amount, notes = notes))
                val newRemaining = loan.remainingAmount - amount
                val newPaid = loan.paidAmount + amount
                val newStatus = if (newRemaining <= 0.01) LoanStatus.CLOSED else loan.status
                app.loanRepository.updateLoan(loan.copy(remainingAmount = maxOf(newRemaining, 0.0), paidAmount = newPaid, status = newStatus))
                _selectedLoan.value = app.loanRepository.getLoanById(loanId)

                _message.value = if (newStatus == LoanStatus.CLOSED) "Loan fully repaid! 🎉" else "Repayment of ₹%.2f recorded".format(amount)
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }

    fun clearMessage() { _message.value = null }
}
