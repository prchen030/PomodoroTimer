package com.example.pomodorotimer.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pomodorotimer.ui.navigation.NavigationGraph
import com.example.pomodorotimer.ui.navigation.Screen

@Composable
fun MainScreen(){
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route ?: Screen.Home.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            when(currentRoute){
                Screen.Home.route -> AppTopBar(Screen.Home, navController)
                Screen.Setting.route -> AppTopBar(Screen.Setting, navController)
                Screen.History.route -> AppTopBar(Screen.History, navController)
            }

        },
    ) { innerPadding ->
        NavigationGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(screen: Screen, navController: NavController){
    TopAppBar(
        title = {
            Text(text = screen.title)
        },
        navigationIcon = {
            DisplayBackButton(screen.displayBackButton, navController)
        },
        actions = {
            DisplayDropdownMenu(screen.displayMenu, navController)
        }
    )
}

@Composable
fun DisplayBackButton(value: Boolean, navController: NavController){
    if (value) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back"
            )
        }
    } else null
}

@Composable
fun DisplayDropdownMenu(value: Boolean, navController: NavController) {
    if (value) {
        DropdownMenu(
            navSettingScreen = {
                navController.navigate(Screen.Setting.route) },
            navHistoryScreen = {
                navController.navigate(Screen.History.route) })
    } else null
}


@Composable
fun DropdownMenu(
    navSettingScreen: () -> Unit,
    navHistoryScreen: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Rounded.Menu, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = "Setting") },
                onClick = { navSettingScreen() }
            )
            DropdownMenuItem(
                text = { Text("History") },
                onClick = { navHistoryScreen() }
            )
        }
    }
}