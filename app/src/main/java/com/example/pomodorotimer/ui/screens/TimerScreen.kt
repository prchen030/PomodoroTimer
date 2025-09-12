package com.example.pomodorotimer.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pomodorotimer.R
import com.example.pomodorotimer.SharedDataViewModel
import com.example.pomodorotimer.TimerStates
import com.example.pomodorotimer.showNotification

@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    viewModel: SharedDataViewModel = viewModel()

){
    TimerView(modifier = modifier, viewModel = viewModel)
}


@Composable
fun TimerView(
    modifier: Modifier,
    viewModel: SharedDataViewModel
){
    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val pomodoro by viewModel.pomodoroTime.collectAsState()
        val shortBreak by viewModel.shortBreakTime.collectAsState()
        val longBreak by viewModel.longBreakTime.collectAsState()

        var secondsElapsed = pomodoro * 60
        var countOfPomodoro = 0
        var isRunning by remember { mutableStateOf(false) }
        var isBreak by remember { mutableStateOf(false) }
        var currTime by remember { mutableStateOf(formatTime(secondsElapsed)) }
        var state by remember { mutableStateOf(TimerStates.POMODORO) }

        TimerText(text = currTime, state = state)

        Row{
            StartButton(
                state = state,
                onClick = {
                    if(!isRunning){
                        isRunning = true
                        startCountDown()
                    }
            })

            StopButton(
                isRunning = isRunning,
                onClick = {
                    isRunning = false
                    stopCountDown()
            })
        }
    }
}


@Composable
fun TimerText(
    state: TimerStates,
    text: String
){
    Text(
        text = text,
        style = TextStyle(
            color = colorResource(state.getColor()),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.1.em,
        )
    )
}

@Composable
fun StartButton(
    state: TimerStates,
    onClick: () -> Unit,
) {
    ElevatedButton(
        onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = colorResource(state.getColor()),
            contentColor = colorResource(R.color.white),
        ),
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = "START")
    }
}

@Composable
fun StopButton(
    isRunning: Boolean,
    onClick: () -> Unit,
){
    ElevatedButton(
        onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = colorResource(R.color.gray),
            contentColor = colorResource(R.color.white),
        ),
        modifier = Modifier.padding(8.dp),
        enabled = isRunning
    ) {
        Text(text = "STOP")
    }
}

// 能不能写一个可以服用的倒计时模块
private fun startCountDown(){

}


private fun stopCountDown(){

}

// If countdown end, send a notification
private fun sendNotification(context: Context, isBreak: Boolean){
    if(isBreak){
        showNotification(context, getString(context, R.string.notification_message_break))
    }else{
        showNotification(context, getString(context, R.string.notification_message_new))
    }
}

private fun checkState(isBreak: Boolean, count: Int): TimerStates {
    return if(!isBreak){
        TimerStates.POMODORO
    }else{
        if(count%2 == 0){
            TimerStates.LONG_BREAK
        }else{
            TimerStates.SHORT_BREAK
        }
    }
}

@SuppressLint("DefaultLocale")
private fun formatTime(secondsElapsed: Int): String {
    val minutes = secondsElapsed / 60
    val seconds = secondsElapsed % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)
    return timeFormatted
}

/*
@Preview(showBackground = true)
@Composable
fun TimerViewPreview(){
    PomodoroTimerTheme {
        TimerView(arrayOf(25, 5, 15))
    }
}*/