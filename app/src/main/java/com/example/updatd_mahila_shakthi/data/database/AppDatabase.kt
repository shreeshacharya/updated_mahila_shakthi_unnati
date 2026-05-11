package com.example.updatd_mahila_shakthi.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.updatd_mahila_shakthi.data.dao.*
import com.example.updatd_mahila_shakthi.data.model.*

/**
 * Main Room database for the Mahila-Shakti Unnati app.
 * Contains 5 tables: Users, Members, Savings, Loans, LoanRepayments.
 */
@Database(
    entities = [
        User::class,
        Member::class,
        Savings::class,
        Loan::class,
        LoanRepayment::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun memberDao(): MemberDao
    abstract fun savingsDao(): SavingsDao
    abstract fun loanDao(): LoanDao
    abstract fun loanRepaymentDao(): LoanRepaymentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mahila_shakti_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
