package com.example.updatd_mahila_shakthi

import android.app.Application
import com.example.updatd_mahila_shakthi.data.database.AppDatabase
import com.example.updatd_mahila_shakthi.data.database.SampleDataInitializer
import com.example.updatd_mahila_shakthi.data.repository.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Application class providing manual dependency injection via lazy-initialized
 * database and repositories. Also initializes sample data on first run.
 */
class MahilaShaktiApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val userRepository by lazy { UserRepository(database.userDao()) }
    val memberRepository by lazy { MemberRepository(database.memberDao()) }
    val savingsRepository by lazy { SavingsRepository(database.savingsDao()) }
    val loanRepository by lazy { LoanRepository(database.loanDao(), database.loanRepaymentDao()) }

    override fun onCreate() {
        super.onCreate()
        // Initialize sample data on first run
        CoroutineScope(Dispatchers.IO).launch {
            SampleDataInitializer.initializeIfEmpty(userRepository, memberRepository, savingsRepository, loanRepository)
        }
    }
}
