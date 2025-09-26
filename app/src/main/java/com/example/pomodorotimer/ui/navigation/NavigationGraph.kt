package com.example.pomodorotimer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pomodorotimer.viewModel.RecordViewModel
import com.example.pomodorotimer.ui.screens.HistoryScreen
import com.example.pomodorotimer.ui.screens.SettingScreen
import com.example.pomodorotimer.ui.screens.TimerScreen
import com.example.pomodorotimer.viewModel.SettingViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val recordViewModel: RecordViewModel = koinViewModel()

    NavHost(
        navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            TimerScreen(recordViewModel = recordViewModel)
        }

        composable(Screen.Setting.route) {
            val settingViewModel: SettingViewModel = koinViewModel()
            SettingScreen(settingViewModel = settingViewModel)
        }

        composable(Screen.History.route) {
            HistoryScreen(recordViewModel = recordViewModel)
        }
    }
}