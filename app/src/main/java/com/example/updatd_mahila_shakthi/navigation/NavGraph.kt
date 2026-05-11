package com.example.updatd_mahila_shakthi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.updatd_mahila_shakthi.ui.screens.*
import com.example.updatd_mahila_shakthi.viewmodel.*

@Composable
fun NavGraph(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel,
    authViewModel: AuthViewModel
) {
    val authState by authViewModel.authState.collectAsState()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route) {
            SplashScreen(onNavigate = {
                navController.navigate(Screen.Login.route) { popUpTo(Screen.Splash.route) { inclusive = true } }
            })
        }

        composable(Screen.Login.route) {
            LoginScreen(
                authState = authState,
                onLogin = { email, pw -> authViewModel.login(email, pw) },
                onNavigateToSignup = { navController.navigate(Screen.Signup.route) },
                onClearError = { authViewModel.clearError() }
            )
            LaunchedEffect(authState.isLoggedIn) {
                if (authState.isLoggedIn) {
                    navController.navigate(Screen.Dashboard.route) { popUpTo(Screen.Login.route) { inclusive = true } }
                }
            }
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                authState = authState,
                onRegister = { n, e, ph, pw, cpw -> authViewModel.register(n, e, ph, pw, cpw) },
                onNavigateBack = { navController.popBackStack() },
                onClearError = { authViewModel.clearError() }
            )
            LaunchedEffect(authState.isLoggedIn) {
                if (authState.isLoggedIn) {
                    navController.navigate(Screen.Dashboard.route) { popUpTo(Screen.Login.route) { inclusive = true } }
                }
            }
        }

        composable(Screen.Dashboard.route) {
            val vm: DashboardViewModel = viewModel()
            DashboardScreen(
                viewModel = vm,
                onNavigate = { route -> navController.navigate(route) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) { inclusive = true } }
                }
            )
        }

        composable(Screen.MemberList.route) {
            val vm: MemberViewModel = viewModel()
            MemberListScreen(vm,
                onAddMember = { navController.navigate(Screen.AddEditMember.createRoute(-1L)) },
                onMemberClick = { navController.navigate(Screen.MemberDetail.createRoute(it)) },
                onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AddEditMember.route, arguments = listOf(navArgument("memberId") { type = NavType.LongType })) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getLong("memberId") ?: -1L
            val vm: MemberViewModel = viewModel()
            AddEditMemberScreen(memberId, vm, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.MemberDetail.route, arguments = listOf(navArgument("memberId") { type = NavType.LongType })) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getLong("memberId") ?: -1L
            val vm: MemberDetailViewModel = viewModel()
            MemberDetailScreen(
                memberId = memberId,
                viewModel = vm,
                onNavigateToRepayment = { navController.navigate(Screen.LoanRepayment.createRoute(it)) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Savings.route) {
            val vm: SavingsViewModel = viewModel()
            SavingsScreen(vm, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.Loans.route) {
            val vm: LoanViewModel = viewModel()
            LoanScreen(vm,
                onRepayment = { navController.navigate(Screen.LoanRepayment.createRoute(it)) },
                onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.LoanRepayment.route, arguments = listOf(navArgument("loanId") { type = NavType.LongType })) { backStackEntry ->
            val loanId = backStackEntry.arguments?.getLong("loanId") ?: 0L
            val vm: LoanRepaymentViewModel = viewModel()
            LoanRepaymentScreen(loanId, vm, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.Reports.route) {
            val vm: ReportViewModel = viewModel()
            ReportsScreen(vm, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.Profile.route) {
            ProfileScreen(authState, onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.Settings.route) {
            SettingsScreen(settingsViewModel, onNavigateBack = { navController.popBackStack() })
        }
    }
}
