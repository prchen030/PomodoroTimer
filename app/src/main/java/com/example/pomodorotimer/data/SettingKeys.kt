package com.example.pomodorotimer.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object SettingKeys {
    val pomodoroMode = intPreferencesKey("pomodoro_mode")
    val shortBreakMode = intPreferencesKey("short_break_mode")
    val longBreakMode = intPreferencesKey("long_break_mode")
    val ifNotification = booleanPreferencesKey("if_notification")
    val ifSound = booleanPreferencesKey("if_sound")
}