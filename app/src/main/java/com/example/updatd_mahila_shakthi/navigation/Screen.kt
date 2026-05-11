package com.example.updatd_mahila_shakthi.navigation

/**
 * Sealed class defining all navigation routes in the app.
 */
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Dashboard : Screen("dashboard")
    object MemberList : Screen("member_list")
    object AddEditMember : Screen("add_edit_member/{memberId}") {
        fun createRoute(memberId: Long = -1L) = "add_edit_member/$memberId"
    }
    object MemberDetail : Screen("member_detail/{memberId}") {
        fun createRoute(memberId: Long) = "member_detail/$memberId"
    }
    object Savings : Screen("savings")
    object Loans : Screen("loans")
    object LoanRepayment : Screen("loan_repayment/{loanId}") {
        fun createRoute(loanId: Long) = "loan_repayment/$loanId"
    }
    object Reports : Screen("reports")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}
