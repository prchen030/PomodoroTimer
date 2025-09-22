package com.example.pomodorotimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pomodorotimer.ui.screens.MainScreen
import com.example.pomodorotimer.ui.theme.PomodoroTimerTheme
import com.example.pomodorotimer.viewModel.RecordViewModel
import com.example.pomodorotimer.viewModel.SettingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    val recordViewModel: RecordViewModel by viewModel()
    val settingViewModel: SettingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PomodoroTimerTheme {
                MainScreen(
                    recordViewModel = recordViewModel,
                    settingViewModel = settingViewModel
                )
            }
        }
    }

}

