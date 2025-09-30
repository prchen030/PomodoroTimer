package com.example.pomodorotimer.ui.navigation

sealed class Screen(val route: String, val title: String, val displayMenu: Boolean, val displayBackButton: Boolean) {
    object Home : Screen("home", "Pomodoro", true, false)
    object Setting : Screen("setting", "Setting", false, true)
    object History : Screen("history", "History", false, true)
}