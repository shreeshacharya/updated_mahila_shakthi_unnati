package com.example.updatd_mahila_shakthi.data.dao

import androidx.room.*
import com.example.updatd_mahila_shakthi.data.model.Loan
import kotlinx.coroutines.flow.Flow

@Dao
interface LoanDao {
    @Insert
    suspend fun insert(loan: Loan): Long

    @Update
    suspend fun update(loan: Loan)

    @Query("SELECT * FROM loans WHERE memberId = :memberId ORDER BY startDate DESC")
    fun getLoansByMember(memberId: Long): Flow<List<Loan>>

    @Query("SELECT * FROM loans WHERE status IN ('PENDING', 'APPROVED', 'ACTIVE') ORDER BY startDate DESC")
    fun getActiveLoans(): Flow<List<Loan>>

    @Query("SELECT EXISTS(SELECT 1 FROM loans WHERE memberId = :memberId AND status IN ('PENDING', 'APPROVED', 'ACTIVE'))")
    suspend fun hasUnpaidLoan(memberId: Long): Boolean

    @Query("SELECT * FROM loans WHERE loanId = :id")
    suspend fun getLoanById(id: Long): Loan?

    @Query("SELECT COUNT(*) FROM loans WHERE status IN ('PENDING', 'APPROVED', 'ACTIVE')")
    fun getActiveLoanCount(): Flow<Int>

    @Query("SELECT * FROM loans ORDER BY startDate DESC")
    fun getAllLoans(): Flow<List<Loan>>

    @Query("SELECT * FROM loans ORDER BY startDate DESC")
    suspend fun getAllLoansList(): List<Loan>
}
