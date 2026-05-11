package com.example.updatd_mahila_shakthi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.updatd_mahila_shakthi.MahilaShaktiApp
import com.example.updatd_mahila_shakthi.data.model.Loan
import com.example.updatd_mahila_shakthi.data.model.LoanStatus
import com.example.updatd_mahila_shakthi.data.model.Member
import com.example.updatd_mahila_shakthi.data.model.PaymentStatus
import com.example.updatd_mahila_shakthi.data.model.Savings
import com.example.updatd_mahila_shakthi.utils.DateUtils
import com.example.updatd_mahila_shakthi.utils.InterestCalculator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MemberDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as MahilaShaktiApp

    private val _memberId = MutableStateFlow<Long>(-1L)

    fun loadMember(memberId: Long) {
        _memberId.value = memberId
    }

    val member: StateFlow<Member?> = _memberId.flatMapLatest { id ->
        flow {
            if (id != -1L) emit(app.memberRepository.getMemberById(id))
            else emit(null)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val totalSavings: StateFlow<Double> = _memberId.flatMapLatest { id ->
        if (id != -1L) app.savingsRepository.getMemberTotalSavings(id) else flowOf(0.0)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val groupTotalSavings: StateFlow<Double> = app.savingsRepository.getTotalSavings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val activeLoans: StateFlow<List<Loan>> = _memberId.flatMapLatest { id ->
        if (id != -1L) app.loanRepository.getLoansByMember(id).map { list -> list.filter { it.status == LoanStatus.ACTIVE } } else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unpaidLoansSum: StateFlow<Double> = activeLoans.map { loans ->
        loans.sumOf { it.remainingAmount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val isPendingThisWeek: StateFlow<Boolean> = _memberId.flatMapLatest { id ->
        if (id != -1L) {
            app.savingsRepository.getSavingsByMember(id).map { savings ->
                val currentWeek = DateUtils.getCurrentWeek()
                val currentYear = DateUtils.getCurrentYear()
                val paidThisWeek = savings.any { it.week == currentWeek && it.year == currentYear && it.status == PaymentStatus.PAID }
                !paidThisWeek
            }
        } else flowOf(false)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun addSavingsEntry(amount: Double, status: PaymentStatus) {
        val currentMemberId = _memberId.value
        if (currentMemberId == -1L) return

        viewModelScope.launch {
            try {
                app.savingsRepository.insert(
                    Savings(
                        memberId = currentMemberId, amount = amount, status = status,
                        week = DateUtils.getCurrentWeek(), month = DateUtils.getCurrentMonth(), year = DateUtils.getCurrentYear()
                    )
                )
                _message.value = "✅ Savings added successfully"
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }

    fun requestLoan(amount: Double, rate: Double, durationMonths: Int) {
        val currentMemberId = _memberId.value
        if (currentMemberId == -1L) return

        viewModelScope.launch {
            try {
                if (app.loanRepository.hasUnpaidLoan(currentMemberId)) {
                    _message.value = "⚠️ Existing unpaid loan detected. Repay the previous loan first."
                    return@launch
                }

                val currentGroupTotalSavings = app.savingsRepository.getAllSavingsList()
                    .filter { it.status == PaymentStatus.PAID }
                    .sumOf { it.amount }

                if (amount >= currentGroupTotalSavings) {
                    _message.value = "⚠️ Loan amount (₹%.0f) must be less than total group savings (₹%.0f)".format(amount, currentGroupTotalSavings)
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
                    Loan(memberId = currentMemberId, loanAmount = amount, interestRate = rate, dueDate = dueDate, remainingAmount = totalRepayment, status = LoanStatus.ACTIVE)
                )
                _message.value = "✅ Loan of ₹%.0f approved".format(amount)
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }

    fun clearMessage() { _message.value = null }
}
