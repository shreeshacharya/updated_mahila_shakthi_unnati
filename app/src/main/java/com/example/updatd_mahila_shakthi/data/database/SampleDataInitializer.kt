package com.example.updatd_mahila_shakthi.data.database

import com.example.updatd_mahila_shakthi.data.model.*
import com.example.updatd_mahila_shakthi.data.repository.*
import com.example.updatd_mahila_shakthi.utils.DateUtils
import com.example.updatd_mahila_shakthi.utils.PasswordUtils

/**
 * Initializes the database with sample data on first run.
 */
object SampleDataInitializer {
    suspend fun initializeIfEmpty(
        userRepo: UserRepository,
        memberRepo: MemberRepository,
        savingsRepo: SavingsRepository,
        loanRepo: LoanRepository
    ) {
        // Only initialize if no members exist
        if (memberRepo.getAllMembersList().isNotEmpty()) return

        // Create demo user
        if (!userRepo.emailExists("admin@shg.com")) {
            userRepo.register(User(name = "Admin User", email = "admin@shg.com", phone = "9876543210", passwordHash = PasswordUtils.hashPassword("admin123")))
        }

        // Create 5 sample members
        val members = listOf(
            Member(name = "Lakshmi Devi", phone = "9876543001", address = "Village Rampur, Block A", weeklyContributionAmount = 100.0),
            Member(name = "Saraswati Kumari", phone = "9876543002", address = "Village Rampur, Block B", weeklyContributionAmount = 100.0),
            Member(name = "Parvati Singh", phone = "9876543003", address = "Village Sundarpur", weeklyContributionAmount = 150.0),
            Member(name = "Durga Mahto", phone = "9876543004", address = "Village Chandpur", weeklyContributionAmount = 100.0),
            Member(name = "Annapurna Das", phone = "9876543005", address = "Village Rampur, Block C", weeklyContributionAmount = 200.0)
        )
        val memberIds = members.map { memberRepo.insert(it) }

        // Add savings entries
        val week = DateUtils.getCurrentWeek()
        val month = DateUtils.getCurrentMonth()
        val year = DateUtils.getCurrentYear()
        memberIds.forEach { id ->
            savingsRepo.insert(Savings(memberId = id, amount = 100.0, status = PaymentStatus.PAID, week = week, month = month, year = year))
        }

        // Add a sample loan for first member
        val dueDate = DateUtils.addMonths(System.currentTimeMillis(), 12)
        loanRepo.insertLoan(Loan(memberId = memberIds[0], loanAmount = 5000.0, interestRate = 12.0, dueDate = dueDate, remainingAmount = 5600.0, status = LoanStatus.ACTIVE))
    }
}
