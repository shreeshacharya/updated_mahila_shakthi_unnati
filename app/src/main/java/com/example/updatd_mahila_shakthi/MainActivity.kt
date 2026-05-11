package com.example.updatd_mahila_shakthi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.updatd_mahila_shakthi.navigation.NavGraph
import com.example.updatd_mahila_shakthi.ui.theme.MahilaShaktiTheme
import com.example.updatd_mahila_shakthi.viewmodel.AuthViewModel
import com.example.updatd_mahila_shakthi.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
            val authViewModel: AuthViewModel = viewModel()

            MahilaShaktiTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    settingsViewModel = settingsViewModel,
                    authViewModel = authViewModel
                )
            }
        }
    }
}