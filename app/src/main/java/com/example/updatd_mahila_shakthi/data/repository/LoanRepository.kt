package com.example.updatd_mahila_shakthi.data.repository

import com.example.updatd_mahila_shakthi.data.dao.LoanDao
import com.example.updatd_mahila_shakthi.data.dao.LoanRepaymentDao
import com.example.updatd_mahila_shakthi.data.model.Loan
import com.example.updatd_mahila_shakthi.data.model.LoanRepayment
import kotlinx.coroutines.flow.Flow

class LoanRepository(
    private val loanDao: LoanDao,
    private val repaymentDao: LoanRepaymentDao
) {
    fun getAllLoans(): Flow<List<Loan>> = loanDao.getAllLoans()
    fun getLoansByMember(memberId: Long): Flow<List<Loan>> = loanDao.getLoansByMember(memberId)
    fun getActiveLoans(): Flow<List<Loan>> = loanDao.getActiveLoans()
    fun getActiveLoanCount(): Flow<Int> = loanDao.getActiveLoanCount()
    suspend fun hasUnpaidLoan(memberId: Long): Boolean = loanDao.hasUnpaidLoan(memberId)
    suspend fun getLoanById(id: Long): Loan? = loanDao.getLoanById(id)
    suspend fun insertLoan(loan: Loan): Long = loanDao.insert(loan)
    suspend fun updateLoan(loan: Loan) = loanDao.update(loan)
    suspend fun getAllLoansList(): List<Loan> = loanDao.getAllLoansList()

    // Repayments
    fun getRepaymentsByLoan(loanId: Long): Flow<List<LoanRepayment>> = repaymentDao.getRepaymentsByLoan(loanId)
    suspend fun getTotalRepaidForLoan(loanId: Long): Double = repaymentDao.getTotalRepaidForLoan(loanId)
    suspend fun insertRepayment(repayment: LoanRepayment): Long = repaymentDao.insert(repayment)
}
