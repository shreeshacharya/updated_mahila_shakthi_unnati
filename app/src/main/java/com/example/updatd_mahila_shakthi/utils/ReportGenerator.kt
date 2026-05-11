package com.example.updatd_mahila_shakthi.utils

import com.example.updatd_mahila_shakthi.data.model.*

object ReportGenerator {
    fun generateReport(
        groupName: String = "Mahila-Shakti Unnati SHG",
        members: List<Member>,
        totalSavings: Double,
        activeLoans: List<Loan>,
        pendingMembers: List<Member>
    ): String {
        val sb = StringBuilder()
        sb.appendLine("🏦 $groupName")
        sb.appendLine("📅 ${DateUtils.formatDate(System.currentTimeMillis())}")
        sb.appendLine()

        sb.appendLine("📊 OVERVIEW")
        sb.appendLine("• Total Members: ${members.size}")
        sb.appendLine("• Total Savings: ₹%.0f".format(totalSavings))
        sb.appendLine("• Active Loans: ${activeLoans.size}")
        sb.appendLine()

        if (pendingMembers.isNotEmpty()) {
            sb.appendLine("⏳ PENDING WEEKLY (₹150)")
            pendingMembers.forEach { member ->
                sb.appendLine("- ${member.name} (${member.phone})")
            }
            sb.appendLine()
        }

        if (activeLoans.isNotEmpty()) {
            sb.appendLine("💰 ACTIVE LOANS")
            activeLoans.forEach { loan ->
                val member = members.find { it.memberId == loan.memberId }
                val memberName = member?.name ?: "Unknown"
                val phoneStr = member?.phone?.let { " ($it)" } ?: ""
                sb.appendLine("- $memberName$phoneStr: ₹%.0f (Rem: ₹%.0f)".format(loan.loanAmount, loan.remainingAmount))
            }
            sb.appendLine()
        }

        sb.appendLine("---")
        sb.appendLine("Mahila-Shakti Unnati")

        return sb.toString()
    }
}
