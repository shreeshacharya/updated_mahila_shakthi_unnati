package com.example.updatd_mahila_shakthi.data.database

import androidx.room.TypeConverter
import com.example.updatd_mahila_shakthi.data.model.LoanStatus
import com.example.updatd_mahila_shakthi.data.model.PaymentStatus

/**
 * Room TypeConverters for enum types stored as strings in the database.
 */
class Converters {
    @TypeConverter
    fun fromPaymentStatus(status: PaymentStatus): String = status.name

    @TypeConverter
    fun toPaymentStatus(value: String): PaymentStatus = PaymentStatus.valueOf(value)

    @TypeConverter
    fun fromLoanStatus(status: LoanStatus): String = status.name

    @TypeConverter
    fun toLoanStatus(value: String): LoanStatus = LoanStatus.valueOf(value)
}
