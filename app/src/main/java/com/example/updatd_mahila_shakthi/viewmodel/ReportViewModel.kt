package com.example.updatd_mahila_shakthi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.updatd_mahila_shakthi.MahilaShaktiApp
import com.example.updatd_mahila_shakthi.utils.ReportGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReportViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as MahilaShaktiApp

    private val _reportText = MutableStateFlow("")
    val reportText: StateFlow<String> = _reportText

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun generateReport() {
        viewModelScope.launch {
            _isLoading.value = true
            val members = app.memberRepository.getAllMembersList()
            val savings = app.savingsRepository.getAllSavingsList()
            val loans = app.loanRepository.getAllLoansList()
            val totalSavings = savings.filter { it.status == com.example.updatd_mahila_shakthi.data.model.PaymentStatus.PAID }.sumOf { it.amount }
            val activeLoans = loans.filter { it.status in listOf(
                com.example.updatd_mahila_shakthi.data.model.LoanStatus.PENDING,
                com.example.updatd_mahila_shakthi.data.model.LoanStatus.APPROVED,
                com.example.updatd_mahila_shakthi.data.model.LoanStatus.ACTIVE
            ) }
            val currentWeek = com.example.updatd_mahila_shakthi.utils.DateUtils.getCurrentWeek()
            val currentYear = com.example.updatd_mahila_shakthi.utils.DateUtils.getCurrentYear()
            val pendingMembers = members.filter { member ->
                val paidThisWeek = savings.filter { 
                    it.memberId == member.memberId && 
                    it.week == currentWeek && 
                    it.year == currentYear && 
                    it.status == com.example.updatd_mahila_shakthi.data.model.PaymentStatus.PAID 
                }.sumOf { it.amount } >= 150.0
                !paidThisWeek
            }

            _reportText.value = ReportGenerator.generateReport(
                members = members, totalSavings = totalSavings, activeLoans = activeLoans, pendingMembers = pendingMembers
            )
            _isLoading.value = false
        }
    }
}
