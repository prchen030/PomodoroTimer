package com.example.pomodorotimer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
    Column (
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Timer(time, modifier)
        ControlButton(onClick = {})
    }
}

@Composable
fun Timer(
    time: String,
    //size : TextUnit,
    modifier: Modifier = Modifier
){
    Text(
        text = time,
        style = TextStyle(
            color = colorResource(R.color.pomodoro_red),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.1.em,
        ),
        modifier = modifier
    )
}

@Composable
fun ControlButton(
    onClick: () -> Unit,
    //点击开始倒计时，同时更新UI：背景变为红色，时间字体和按钮为白色，按钮字体为红色
) {
    ElevatedButton(
        onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = colorResource(R.color.pomodoro_red),
            contentColor = colorResource(R.color.white),
        ),
    ) {
        Image(
            painterResource(id = R.drawable.outline_play_arrow_24),
            contentDescription ="Start timing",
            modifier = Modifier.size(20.dp)
        )
        Text(text = "Start")

    }
}

@Preview(showBackground = true)
@Composable
fun TimerViewPreview(){
    PomodoroTimerTheme {
        TimerView("25:00")
    }
}