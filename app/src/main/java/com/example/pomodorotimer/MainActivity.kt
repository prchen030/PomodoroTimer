package com.example.pomodorotimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import com.example.pomodorotimer.ui.screens.MainScreen
import com.example.pomodorotimer.ui.theme.PomodoroTimerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {

    val recordViewModel: RecordViewModel by viewModel()
    val sharedDataViewModel: SharedDataViewModel by viewModel{ parametersOf(LocalContext)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PomodoroTimerTheme {
                MainScreen(
                    recordViewModel = recordViewModel,
                    sharedDataViewModel = sharedDataViewModel
                )
            }
        }
    }
}

