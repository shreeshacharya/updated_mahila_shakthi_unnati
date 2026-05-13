package com.example.updatd_mahila_shakthi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.updatd_mahila_shakthi.MahilaShaktiApp
import kotlinx.coroutines.flow.Flow

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as MahilaShaktiApp

    val totalMembers: Flow<Int> = app.memberRepository.getTotalMemberCount()
    val totalSavings: Flow<Double> = app.savingsRepository.getTotalSavings()
    val activeLoans: Flow<Int> = app.loanRepository.getActiveLoanCount()
    val pendingPayments: Flow<Int> = kotlinx.coroutines.flow.combine(
        app.memberRepository.getAllMembers(),
        app.savingsRepository.getAllSavings()
    ) { members, savings ->
        val currentWeek = com.example.updatd_mahila_shakthi.utils.DateUtils.getCurrentWeek()
        val currentYear = com.example.updatd_mahila_shakthi.utils.DateUtils.getCurrentYear()
        var pendingCount = 0
        members.forEach { member ->
            val paidThisWeek = savings.filter { 
                it.memberId == member.memberId && 
                it.week == currentWeek && 
                it.year == currentYear && 
                it.status == com.example.updatd_mahila_shakthi.data.model.PaymentStatus.PAID 
            }.sumOf { it.amount } >= 150.0
            if (!paidThisWeek) pendingCount++
        }
        pendingCount
    }
}
