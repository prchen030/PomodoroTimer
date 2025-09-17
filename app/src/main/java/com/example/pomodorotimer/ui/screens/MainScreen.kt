package com.example.pomodorotimer.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pomodorotimer.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(){
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route ?: Screen.Home.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppToolbar(
                title  = when (currentRoute) {
                    Screen.Home.route -> Screen.Home.title
                    Screen.Setting.route -> Screen.Setting.title
                    Screen.History.route -> Screen.History.title
                    else -> ""
                },
                showBackButton = when(currentRoute){
                    Screen.Setting.route -> true
                    Screen.History.route -> true
                    else -> false
                },
                showMenuButton = when(currentRoute){
                    Screen.Home.route -> true
                    else -> false
                },
                onBackClick = { navController.popBackStack() }
            )
        },
    ) { innerPadding ->
        NavigationGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}