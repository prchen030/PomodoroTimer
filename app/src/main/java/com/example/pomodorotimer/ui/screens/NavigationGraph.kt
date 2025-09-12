package com.example.pomodorotimer.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
            SettingScreen(navController = navController)
        }

        composable(Screen.History.route) {
            HistoryScreen()
        }

        composable(
            route = Screen.Edit.route,
            arguments = listOf(navArgument("key") { type = NavType.StringType })
        ) { backStackEntry ->
            val key = backStackEntry.arguments?.getString("key") ?: ""
            EditTextScreen(key = key)
        }
    }
}