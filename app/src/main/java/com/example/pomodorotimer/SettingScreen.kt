package com.example.pomodorotimer

import android.content.Context
import android.media.AudioManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pomodorotimer.ui.theme.PomodoroTimerTheme

@Composable
fun SettingView(
    modifier: Modifier = Modifier
){
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TimerSetting()
        NotificationSetting()
    }
}

@Composable
fun TimerSetting(){
    Column(modifier = Modifier.padding(10.dp)){
        Text(text = "TIMER", modifier = Modifier.padding(10.dp))
        Card{
            Column{
                SettingRowWithTextField(stringResource(R.string.label_pomodoro), "25")
                CustomHorizontalDivider()
                SettingRowWithTextField(stringResource(R.string.label_short_break), "5")
                CustomHorizontalDivider()
                SettingRowWithTextField(stringResource(R.string.label_long_break),  "15")
            }
        }

    }
}

@Composable
fun NotificationSetting(){
    Column(
        modifier = Modifier.padding(10.dp)
    ){
        Text(text = "NOTIFICATION", modifier = Modifier.padding(10.dp))
        Card{
            SettingRowWithSwitch(stringResource(R.string.label_system_notification))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Card{
            Column {
                SettingRowWithSwitch(stringResource(R.string.label_alert_sound))
                CustomHorizontalDivider()

                // Use a slider to control volume
                val context = LocalContext.current
                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                var volume by remember { mutableIntStateOf(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)) }
                Slider(
                    value = volume.toFloat(),
                    onValueChange = {
                        volume = it.toInt()
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
                    },
                    steps = 5,
                    valueRange = 0f..maxVolume.toFloat()
                )
            }
        }
    }
}

@Composable
fun SettingRowWithTextField(label: String, value: String){
    Row(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = label, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.weight(1f))

        var text by remember { mutableStateOf(value) }
        TextField(
            value = text,
            onValueChange = { newText ->
                text = newText
            },
            suffix = { Text(" min") },
            singleLine = true,
            modifier = Modifier.weight(0.8f),
            textStyle = TextStyle(textAlign = TextAlign.Left),
        )
    }
}

@Composable
fun SettingRowWithSwitch(text: String){
    Row(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        var checked by remember { mutableStateOf(true) }
        Text( text = text, modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            }
        )
    }
}

@Composable
fun CustomHorizontalDivider(){
    HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(12.dp, 0.dp))
}

@Preview(showBackground = true)
@Composable
fun SettingViewPreview(){
    PomodoroTimerTheme {
        SettingView()
    }
}