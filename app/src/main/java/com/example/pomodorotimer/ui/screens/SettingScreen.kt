package com.example.pomodorotimer.ui.screens

import android.content.Context
import android.media.AudioManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pomodorotimer.PrefKeys
import com.example.pomodorotimer.RequestNotificationPermission
import com.example.pomodorotimer.SharedDataViewModel
import com.example.pomodorotimer.createNotificationChannel

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    sharedDataViewModel: SharedDataViewModel
){
    SettingView(modifier = modifier, viewModel = sharedDataViewModel)
}


@Composable
fun SettingView(
    modifier: Modifier,
    viewModel: SharedDataViewModel,
){
    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TimerSetting(modifier = modifier, viewModel = viewModel)
        NotificationSetting(modifier = modifier, viewModel = viewModel)
    }
}

@Composable
fun TimerSetting(
    modifier: Modifier,
    viewModel: SharedDataViewModel,
){
    Column(modifier = modifier.padding(10.dp)){
        Text(text = "TIMER", modifier = Modifier.padding(10.dp))
        Card{
            Column{
                SettingRowWithText(modifier,PrefKeys.KEY_POMODORO_TIME, viewModel)
                CustomHorizontalDivider()
                SettingRowWithText(modifier,PrefKeys.KEY_SHORT_BREAK_TIME,  viewModel)
                CustomHorizontalDivider()
                SettingRowWithText(modifier,PrefKeys.KEY_LONG_BREAK_TIME, viewModel)
            }
        }
    }
}

@Composable
fun NotificationSetting(
    modifier: Modifier,
    viewModel: SharedDataViewModel
){
    Column(
        modifier = modifier.padding(10.dp)
    ){
        Text(text = "NOTIFICATION", modifier = modifier.padding(10.dp))
        Card{
            val checked by viewModel.ifNotification.collectAsState()
            val context = LocalContext.current
            if(checked) RequestNotificationPermission(context)

            SettingRowWithSwitch(PrefKeys.KEY_IF_NOTIFICATION, checked, viewModel)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Card{
            Column {
                val checked by viewModel.ifSound.collectAsState()
                SettingRowWithSwitch(PrefKeys.KEY_IF_SOUND, checked,viewModel)
                CustomHorizontalDivider()
                // Use a slider to control volume
                val context = LocalContext.current
                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                var volume by remember {
                    mutableIntStateOf(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)) }
                Slider(
                    value = volume.toFloat(),
                    onValueChange = {
                        volume = it.toInt()
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
                    },
                    steps = 5,
                    valueRange = 0f..maxVolume.toFloat(),
                    enabled = checked
                )
            }
        }
    }
}

@Composable
fun SettingRowWithText(modifier: Modifier, key: String, viewModel: SharedDataViewModel){
    var showDialog by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .clickable(onClick = {
                showDialog = true
            })
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = key, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.weight(1f))

        var value = viewModel.getIntValueByKey(key).toString()
        Text(text ="$value min", modifier = modifier.weight(0.8f))

        if(showDialog){
            EditDurationDialog(
                modifier = modifier,
                key = key,
                viewModel = viewModel,
                onConfirm = { newText ->
                    value = newText
                    showDialog = false
                },
                onDismiss = {
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun EditDurationDialog(
    modifier: Modifier,
    key: String,
    viewModel: SharedDataViewModel,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val value = viewModel.getIntValueByKey(key)
    var text by remember { mutableStateOf(value.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("") },
        text = {
            Column {
                Text("Enter some text:")
                TextField(
                    value = text,
                    onValueChange = { newText ->
                        text = newText
                        viewModel.updateIntValue(key, text.toInt())
                    },
                    suffix = { Text(" min") },
                    singleLine = true,
                    modifier = modifier.fillMaxWidth().padding(48.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Left),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(text)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun SettingRowWithSwitch(key: String, value: Boolean, viewModel: SharedDataViewModel){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        var checked by remember { mutableStateOf(value) }
        Text( text = key, modifier = Modifier.weight(1f))
        val context = LocalContext.current
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
                viewModel.updateBooleanValue(key, it)
                if(key == PrefKeys.KEY_IF_NOTIFICATION && checked){
                    createNotificationChannel(context)
                }
            }
        )
    }
}

@Composable
fun CustomHorizontalDivider(){
    HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(12.dp, 0.dp))
}

