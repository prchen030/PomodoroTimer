package com.example.pomodorotimer

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.pomodorotimer.ui.theme.PomodoroTimerTheme


@Composable
fun TimerView(
    time: String,
    modifier: Modifier = Modifier
){
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val handler = Handler(Looper.getMainLooper())
        var secondsElapsed = timeToSecond(time)
        var isRunning by remember { mutableStateOf(false) }
        var isBreak by remember { mutableStateOf(false) }
        var currTime by remember { mutableStateOf(time) }

        val updateTime: Runnable = object : Runnable {
            override fun run() {
                if(!isRunning) return
                currTime = formatTime(secondsElapsed)
                secondsElapsed--
                if(secondsElapsed == -1){
                    isBreak = !isBreak
                    isRunning = false
                    handler.removeCallbacksAndMessages(null)
                }else{
                    handler.postDelayed(this, 1000)
                }
            }
        }

        Text(
            text = currTime,
            style = TextStyle(
                color = if(isBreak) colorResource(R.color.short_break_green) else colorResource(R.color.pomodoro_red),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.1.em,
            ),
            modifier = modifier
        )

        Row{
            StartButton(
                isBreak = isBreak,
                onClick = {
                    if(!isRunning){
                        isRunning = true
                        handler.postAtTime(updateTime, 1000)
                    }
            })

            StopButton(
                isRunning = isRunning,
                onClick = {
                    isRunning = false
                    handler.removeCallbacks(updateTime)
                    Log.i("StopButton", secondsElapsed.toString())
            })
        }
    }
}

@Composable
fun StartButton(
    isBreak: Boolean,
    onClick: () -> Unit,
) {
    ElevatedButton(
        onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = if(isBreak) colorResource(R.color.short_break_green) else colorResource(R.color.pomodoro_red),
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

private fun timeToSecond(time: String): Int {
    val parts = time.split(":")
    val minutes = parts[0].toInt()
    val seconds = parts[1].toInt()
    return minutes * 60 + seconds
}

@SuppressLint("DefaultLocale")
private fun formatTime(secondsElapsed: Int): String {
    val minutes = secondsElapsed / 60
    val seconds = secondsElapsed % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)
    return timeFormatted
}

@Preview(showBackground = true)
@Composable
fun TimerViewPreview(){
    PomodoroTimerTheme {
        TimerView(stringResource(R.string.default_duration))
    }
}