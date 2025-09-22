package com.example.pomodorotimer.ui.screens

import android.content.Context
import android.media.AudioManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import com.example.pomodorotimer.R
import com.example.pomodorotimer.units.PrefKeys
import com.example.pomodorotimer.units.RequestNotificationPermission
import com.example.pomodorotimer.viewModel.SettingViewModel
import com.example.pomodorotimer.units.createNotificationChannel

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    settingViewModel: SettingViewModel
){
    SettingView(modifier = modifier, viewModel = settingViewModel)
}


@Composable
fun SettingView(
    modifier: Modifier,
    viewModel: SettingViewModel,
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
    viewModel: SettingViewModel,
){
    Column(modifier = modifier.padding(10.dp)){
        Text(text = "TIMER", modifier = Modifier.padding(10.dp))
        Card{
            Column{

                val pomodoroTime by viewModel.pomodoroTime.collectAsState()
                val shortBreakTime by viewModel.shortBreakTime.collectAsState()
                val longBreakTime by viewModel.longBreakTime.collectAsState()

                SettingRowWithText(modifier,PrefKeys.KEY_POMODORO_TIME, viewModel, pomodoroTime)
                CustomHorizontalDivider()
                SettingRowWithText(modifier,PrefKeys.KEY_SHORT_BREAK_TIME,  viewModel, shortBreakTime)
                CustomHorizontalDivider()
                SettingRowWithText(modifier,PrefKeys.KEY_LONG_BREAK_TIME, viewModel, longBreakTime)

            }
        }
    }
}

@Composable
fun NotificationSetting(
    modifier: Modifier,
    viewModel: SettingViewModel
){
    Column(
        modifier = modifier.padding(10.dp)
    ){
        Text(text = "NOTIFICATION", modifier = modifier.padding(10.dp))
        Card{
            val checked by viewModel.ifNotification.collectAsState()
            val isGranted by viewModel.isGranted.collectAsState()
            val context = LocalContext.current
            if(checked && !isGranted) {
                RequestNotificationPermission(context)
                viewModel.setGrantValue()
            }
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
fun SettingRowWithText(
    modifier: Modifier,
    key: String,
    viewModel: SettingViewModel,
    text: Int
){
    var showDialog by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .clickable(onClick = {
                showDialog = true
            })
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Text(text = key)
        Spacer(modifier = Modifier.weight(1f))

        var value = text.toString()
        Text(text ="$value min", modifier = modifier)

        if(showDialog){
            EditDurationDialog(
                modifier = modifier,
                value = value,
                onConfirm = { newText ->
                    value = newText
                    viewModel.updateIntValue(key, value.toInt())
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
    value: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    var text by remember { mutableStateOf(value) }
    val isValid = text.isNotEmpty() &&
            text.toInt() > 0 &&
            text.length <= 3 &&
            text.all { it.isDigit() }

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
                    },
                    suffix = { Text(" min") },
                    singleLine = true,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Left),
                    isError = !isValid,
                    supportingText = {
                        if (!isValid) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = getString(context, R.string.error_msg_input),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    trailingIcon = {
                        if (!isValid)
                            Icon(Icons.Rounded.Close,"error", tint = MaterialTheme.colorScheme.error)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if(isValid){
                        onConfirm(text)
                    }
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
fun SettingRowWithSwitch(
    key: String,
    value: Boolean,
    viewModel: SettingViewModel
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text( text = key, modifier = Modifier.weight(1f))

        val context = LocalContext.current
        var checked by remember { mutableStateOf(value) }
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
    HorizontalDivider(
        thickness = 1.dp,
        modifier = Modifier
            .padding(12.dp, 0.dp)
    )
}

