package com.example.updatd_mahila_shakthi.data.repository

import com.example.updatd_mahila_shakthi.data.dao.SavingsDao
import com.example.updatd_mahila_shakthi.data.model.Savings
import kotlinx.coroutines.flow.Flow

class SavingsRepository(private val savingsDao: SavingsDao) {
    fun getAllSavings(): Flow<List<Savings>> = savingsDao.getAllSavings()
    fun getSavingsByMember(memberId: Long): Flow<List<Savings>> = savingsDao.getSavingsByMember(memberId)
    fun getTotalSavings(): Flow<Double> = savingsDao.getTotalSavings()
    fun getMemberTotalSavings(memberId: Long): Flow<Double> = savingsDao.getMemberTotalSavings(memberId)
    fun getPendingPaymentsCount(): Flow<Int> = savingsDao.getPendingPaymentsCount()
    suspend fun insert(savings: Savings): Long = savingsDao.insert(savings)
    suspend fun update(savings: Savings) = savingsDao.update(savings)
    suspend fun getAllSavingsList(): List<Savings> = savingsDao.getAllSavingsList()
}
