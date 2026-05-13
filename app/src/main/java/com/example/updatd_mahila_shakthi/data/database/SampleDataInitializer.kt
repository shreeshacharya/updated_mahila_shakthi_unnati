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

    }
}
