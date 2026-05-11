package com.example.updatd_mahila_shakthi.data.dao

import androidx.room.*
import com.example.updatd_mahila_shakthi.data.model.LoanRepayment
import kotlinx.coroutines.flow.Flow

@Dao
interface LoanRepaymentDao {
    @Insert
    suspend fun insert(repayment: LoanRepayment): Long

    @Query("SELECT * FROM loan_repayments WHERE loanId = :loanId ORDER BY date DESC")
    fun getRepaymentsByLoan(loanId: Long): Flow<List<LoanRepayment>>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM loan_repayments WHERE loanId = :loanId")
    suspend fun getTotalRepaidForLoan(loanId: Long): Double
}
