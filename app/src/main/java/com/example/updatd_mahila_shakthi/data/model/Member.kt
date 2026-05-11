package com.example.updatd_mahila_shakthi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Member entity representing a self-help group member.
 */
@Entity(tableName = "members")
data class Member(
    @PrimaryKey(autoGenerate = true)
    val memberId: Long = 0,
    val name: String,
    val phone: String,
    val address: String,
    val aadhaarNumber: String = "",
    val profileImagePath: String = "",
    val joinDate: Long = System.currentTimeMillis(),
    val weeklyContributionAmount: Double = 150.0,
    val isActive: Boolean = true
)
