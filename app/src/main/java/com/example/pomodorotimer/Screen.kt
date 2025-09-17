package com.example.pomodorotimer

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Pomodoro")
    object Setting : Screen("setting", "Setting")
    object History : Screen("history", "History")
}