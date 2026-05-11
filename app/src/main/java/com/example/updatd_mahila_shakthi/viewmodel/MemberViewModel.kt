package com.example.updatd_mahila_shakthi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.updatd_mahila_shakthi.MahilaShaktiApp
import com.example.updatd_mahila_shakthi.data.model.Member
import com.example.updatd_mahila_shakthi.data.model.MemberStats
import com.example.updatd_mahila_shakthi.data.model.PaymentStatus
import com.example.updatd_mahila_shakthi.utils.DateUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MemberViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as MahilaShaktiApp).memberRepository

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val members: StateFlow<List<Member>> = _searchQuery.flatMapLatest { query ->
        if (query.isBlank()) repository.getAllMembers() else repository.searchMembers(query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val memberStats: StateFlow<List<MemberStats>> = combine(
        members,
        (application as MahilaShaktiApp).savingsRepository.getAllSavings(),
        (application as MahilaShaktiApp).loanRepository.getActiveLoans()
    ) { membersList, savingsList, activeLoansList ->
        val currentWeek = DateUtils.getCurrentWeek()
        val currentYear = DateUtils.getCurrentYear()

        membersList.map { member ->
            val memberSavings = savingsList.filter { it.memberId == member.memberId && it.status == PaymentStatus.PAID }
            val totalSaved = memberSavings.sumOf { it.amount }
            val paidThisWeek = memberSavings.any { it.week == currentWeek && it.year == currentYear }
            val hasActiveLoan = activeLoansList.any { it.memberId == member.memberId }

            MemberStats(member, totalSaved, paidThisWeek, hasActiveLoan)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedMember = MutableStateFlow<Member?>(null)
    val selectedMember: StateFlow<Member?> = _selectedMember

    private val _operationMessage = MutableStateFlow<String?>(null)
    val operationMessage: StateFlow<String?> = _operationMessage

    fun onSearchQueryChange(query: String) { _searchQuery.value = query }

    fun loadMember(memberId: Long) {
        viewModelScope.launch { _selectedMember.value = repository.getMemberById(memberId) }
    }

    fun saveMember(name: String, phone: String, address: String, profileImagePath: String, contribution: Double, existingId: Long = -1L) {
        viewModelScope.launch {
            try {
                if (existingId > 0) {
                    val existing = repository.getMemberById(existingId)
                    if (existing != null) {
                        repository.update(existing.copy(name = name, phone = phone, address = address, profileImagePath = profileImagePath, weeklyContributionAmount = contribution))
                        _operationMessage.value = "Member updated successfully"
                    }
                } else {
                    repository.insert(Member(name = name, phone = phone, address = address, profileImagePath = profileImagePath, weeklyContributionAmount = contribution))
                    _operationMessage.value = "Member added successfully"
                }
            } catch (e: Exception) {
                _operationMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun deleteMember(member: Member) {
        viewModelScope.launch {
            repository.delete(member)
            _operationMessage.value = "${member.name} deleted"
        }
    }

    fun clearMessage() { _operationMessage.value = null }
}
