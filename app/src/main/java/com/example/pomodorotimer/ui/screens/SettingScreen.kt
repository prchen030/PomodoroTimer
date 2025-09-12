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
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.pomodorotimer.PrefKeys
import com.example.pomodorotimer.R
import com.example.pomodorotimer.RequestNotificationPermission
import com.example.pomodorotimer.Screen
import com.example.pomodorotimer.SharedDataViewModel
import com.example.pomodorotimer.createNotificationChannel
import com.example.pomodorotimer.showNotification

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    viewModel: SharedDataViewModel = viewModel(),
    navController: NavHostController
){
    SettingView(modifier = modifier, viewModel = viewModel, navController = navController)
}


@Composable
fun SettingView(
    modifier: Modifier,
    viewModel: SharedDataViewModel,
    navController: NavHostController
){
    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TimerSetting(modifier = modifier, viewModel = viewModel, navController = navController)
        NotificationSetting(modifier = modifier, viewModel = viewModel)
    }
}

@Composable
fun TimerSetting(
    modifier: Modifier,
    viewModel: SharedDataViewModel,
    navController: NavHostController
){
    Column(modifier = modifier.padding(10.dp)){
        Text(text = "TIMER", modifier = Modifier.padding(10.dp))
        Card{
            Column{
                SettingRowWithText(PrefKeys.KEY_POMODORO_TIME, viewModel, navController)
                CustomHorizontalDivider()
                SettingRowWithText(PrefKeys.KEY_SHORT_BREAK_TIME,  viewModel, navController)
                CustomHorizontalDivider()
                SettingRowWithText(PrefKeys.KEY_LONG_BREAK_TIME, viewModel, navController)
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
fun SettingRowWithText(key: String, viewModel: SharedDataViewModel, navController: NavHostController){
    Row(
        modifier = Modifier
            .clickable(onClick = {
                navController.navigate(Screen.Edit.createRoute(key))
            })
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = key, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.weight(1f))

        val value = viewModel.getIntValueByKey(key)
        Text(text ="$value min", modifier = Modifier.weight(0.8f))
    }
}

@Composable
fun SettingRowWithSwitch(key: String, value: Boolean, viewModel: SharedDataViewModel){
    Row(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
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

/*
@Preview(showBackground = true)
@Composable
fun SettingViewPreview(){
    PomodoroTimerTheme {
        SettingView(viewModel = viewModel())
    }
}*/
