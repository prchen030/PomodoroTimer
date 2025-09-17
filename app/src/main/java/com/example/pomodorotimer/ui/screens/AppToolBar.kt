package com.example.pomodorotimer.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pomodorotimer.ui.theme.PomodoroTimerTheme

@Composable
fun AppToolbar(
    title: String,
    showBackButton: Boolean = false,
    showMenuButton: Boolean = true,
    onBackClick: (() -> Unit)? = null,
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = if (showBackButton && onBackClick != null) {
            {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                }
            }
        } else null,
        actions = {
            if(showMenuButton) {
                DropdownMenu({}, {})
            }
        }
    )
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


@Preview(showBackground = true)
@Composable
fun AppToolBarPreview(){
    PomodoroTimerTheme {
        AppToolbar(
            title="Pomodoro",
            showBackButton = true,
            showMenuButton = false,
            onBackClick = {}
        )
    }
}

