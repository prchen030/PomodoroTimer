package com.example.pomodorotimer.ui.screens

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.pomodorotimer.R
import com.example.pomodorotimer.RecordViewModel
import com.example.pomodorotimer.TimerStates
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    recordViewModel: RecordViewModel
){
    TimerView(modifier = modifier, recordViewModel = recordViewModel)
}


@Composable
fun TimerView(
    modifier: Modifier,
    recordViewModel: RecordViewModel
){
    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        var _isRunning by remember { mutableStateOf(false) }

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.now().format(formatter)
        val state by recordViewModel.state.collectAsState()
        val duration by recordViewModel.timeLeft.collectAsState()
        val secondsElapsed by remember { mutableIntStateOf(duration * 60) }
        var currTime by remember { mutableStateOf(formatTime(secondsElapsed)) }
        val context = LocalContext.current

        Log.i("TimeScreen", secondsElapsed.toString())

        TimerText(text = currTime, state = state)

        Row{
            StartButton(
                state = state,
                onClick = {
                    if(!_isRunning){
                        _isRunning = true
                        recordViewModel.startCountdown(date = today, context = context)
                    }
            })

            PauseButton(
                isRunning = _isRunning,
                onClick = {
                    _isRunning = false
                    recordViewModel.pauseCountdown()
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
fun PauseButton(
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
        Text(text = "PAUSE")
    }
}

@SuppressLint("DefaultLocale")
private fun formatTime(secondsElapsed: Int): String {
    val minutes = secondsElapsed / 60
    val seconds = secondsElapsed % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)
    return timeFormatted
}