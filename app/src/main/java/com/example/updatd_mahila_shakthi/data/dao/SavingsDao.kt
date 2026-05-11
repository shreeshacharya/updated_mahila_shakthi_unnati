package com.example.updatd_mahila_shakthi.data.dao

import androidx.room.*
import com.example.updatd_mahila_shakthi.data.model.Savings
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingsDao {
    @Insert
    suspend fun insert(savings: Savings): Long

    @Update
    suspend fun update(savings: Savings)

    @Query("SELECT * FROM savings WHERE memberId = :memberId ORDER BY date DESC")
    fun getSavingsByMember(memberId: Long): Flow<List<Savings>>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM savings WHERE status = 'PAID'")
    fun getTotalSavings(): Flow<Double>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM savings WHERE memberId = :memberId AND status = 'PAID'")
    fun getMemberTotalSavings(memberId: Long): Flow<Double>

    @Query("SELECT * FROM savings ORDER BY date DESC")
    fun getAllSavings(): Flow<List<Savings>>

    @Query("SELECT COUNT(*) FROM savings WHERE status = 'PENDING'")
    fun getPendingPaymentsCount(): Flow<Int>

    @Query("SELECT * FROM savings ORDER BY date DESC")
    suspend fun getAllSavingsList(): List<Savings>
}
