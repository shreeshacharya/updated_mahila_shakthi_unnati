package com.example.updatd_mahila_shakthi.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Loan status lifecycle enum.
 */
enum class LoanStatus {
    PENDING, APPROVED, ACTIVE, CLOSED, DEFAULTED
}

/**
 * Loan entity for tracking member loans.
 * One Member → Many Loans (but business rule: only one active at a time).
 */
@Entity(
    tableName = "loans",
    foreignKeys = [
        ForeignKey(
            entity = Member::class,
            parentColumns = ["memberId"],
            childColumns = ["memberId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("memberId")]
)
data class Loan(
    @PrimaryKey(autoGenerate = true)
    val loanId: Long = 0,
    val memberId: Long,
    val loanAmount: Double,
    val interestRate: Double,
    val startDate: Long = System.currentTimeMillis(),
    val dueDate: Long,
    val remainingAmount: Double,
    val paidAmount: Double = 0.0,
    val status: LoanStatus = LoanStatus.PENDING
)
