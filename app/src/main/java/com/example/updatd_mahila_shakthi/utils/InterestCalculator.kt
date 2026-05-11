package com.example.updatd_mahila_shakthi.utils

object InterestCalculator {
    /**
     * Simple Interest = (P × R × T) / 100
     * @param principal Loan amount
     * @param rate Annual interest rate (%)
     * @param timeInMonths Loan duration in months
     */
    fun calculateSimpleInterest(principal: Double, rate: Double, timeInMonths: Int): Double {
        val timeInYears = timeInMonths / 12.0
        return (principal * rate * timeInYears) / 100.0
    }

    fun calculateTotalRepayment(principal: Double, rate: Double, timeInMonths: Int): Double {
        return principal + calculateSimpleInterest(principal, rate, timeInMonths)
    }
}
