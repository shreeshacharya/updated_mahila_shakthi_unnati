package com.example.updatd_mahila_shakthi.data.model

data class MemberStats(
    val member: Member,
    val totalSavings: Double,
    val paidThisWeek: Boolean,
    val hasActiveLoan: Boolean
)
