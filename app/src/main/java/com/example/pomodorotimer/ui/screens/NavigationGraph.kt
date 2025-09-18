package com.example.pomodorotimer.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pomodorotimer.RecordViewModel
import com.example.pomodorotimer.Screen
import com.example.pomodorotimer.SharedDataViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    recordViewModel: RecordViewModel,
    sharedDataViewModel: SharedDataViewModel
) {
    NavHost(
        navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            TimerScreen(recordViewModel = recordViewModel)
        }

        composable(Screen.Setting.route) {
            SettingScreen(sharedDataViewModel = sharedDataViewModel)
        }

        composable(Screen.History.route) {
            HistoryScreen(recordViewModel = recordViewModel)
        }
    }
}