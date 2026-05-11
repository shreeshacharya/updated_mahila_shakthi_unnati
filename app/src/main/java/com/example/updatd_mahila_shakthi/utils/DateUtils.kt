package com.example.updatd_mahila_shakthi.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val displayFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val shortFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

    fun formatDate(timestamp: Long): String = displayFormat.format(Date(timestamp))
    fun formatDateShort(timestamp: Long): String = shortFormat.format(Date(timestamp))

    fun getCurrentWeek(): Int {
        val cal = Calendar.getInstance()
        return cal.get(Calendar.WEEK_OF_YEAR)
    }

    fun getCurrentMonth(): Int {
        val cal = Calendar.getInstance()
        return cal.get(Calendar.MONTH) + 1
    }

    fun getCurrentYear(): Int {
        val cal = Calendar.getInstance()
        return cal.get(Calendar.YEAR)
    }

    fun getMonthsBetween(startDate: Long, endDate: Long): Int {
        val start = Calendar.getInstance().apply { timeInMillis = startDate }
        val end = Calendar.getInstance().apply { timeInMillis = endDate }
        val months = (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12 +
                (end.get(Calendar.MONTH) - start.get(Calendar.MONTH))
        return maxOf(months, 1)
    }

    fun addMonths(timestamp: Long, months: Int): Long {
        val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
        cal.add(Calendar.MONTH, months)
        return cal.timeInMillis
    }
}
