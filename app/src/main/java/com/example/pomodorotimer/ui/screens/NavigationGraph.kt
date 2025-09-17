package com.example.pomodorotimer.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pomodorotimer.Screen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {

        composable(Screen.Home.route) {
            TimerScreen()
        }

        composable(Screen.Setting.route) {
            SettingScreen()
        }

        composable(Screen.History.route) {
            HistoryScreen()
        }
    }
}