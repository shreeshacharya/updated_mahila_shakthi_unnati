package com.example.updatd_mahila_shakthi.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Payment status enum for savings entries.
 */
enum class PaymentStatus {
    PAID, PENDING
}

/**
 * Savings entity representing a weekly savings contribution by a member.
 * One Member → Many Savings entries.
 */
@Entity(
    tableName = "savings",
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
data class Savings(
    @PrimaryKey(autoGenerate = true)
    val savingsId: Long = 0,
    val memberId: Long,
    val amount: Double,
    val date: Long = System.currentTimeMillis(),
    val status: PaymentStatus = PaymentStatus.PAID,
    val week: Int,
    val month: Int,
    val year: Int
)
