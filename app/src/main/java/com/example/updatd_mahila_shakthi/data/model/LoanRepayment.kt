package com.example.updatd_mahila_shakthi.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Loan repayment entity tracking individual payments against a loan.
 * One Loan → Many Repayments.
 */
@Entity(
    tableName = "loan_repayments",
    foreignKeys = [
        ForeignKey(
            entity = Loan::class,
            parentColumns = ["loanId"],
            childColumns = ["loanId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("loanId")]
)
data class LoanRepayment(
    @PrimaryKey(autoGenerate = true)
    val repaymentId: Long = 0,
    val loanId: Long,
    val amount: Double,
    val date: Long = System.currentTimeMillis(),
    val notes: String = ""
)
