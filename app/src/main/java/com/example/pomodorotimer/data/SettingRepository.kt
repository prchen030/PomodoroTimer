package com.example.pomodorotimer.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.pomodorotimer.model.TimerStates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingRepository(
    private val dataStore: DataStore<Preferences>
) {
    val pomodoroMode: Flow<Int> = dataStore.data
        .map { prefs -> prefs[SettingKeys.pomodoroMode] ?: TimerStates.POMODORO.default }

    val shortBreakMode: Flow<Int> = dataStore.data
        .map { prefs -> prefs[SettingKeys.shortBreakMode] ?: TimerStates.SHORT_BREAK.default }

    val longBreakMode: Flow<Int> = dataStore.data
        .map { prefs -> prefs[SettingKeys.longBreakMode] ?: TimerStates.LONG_BREAK.default }

    val ifNotification: Flow<Boolean> = dataStore.data
    .map { prefs -> prefs[SettingKeys.ifNotification] ?: false }

    val ifSound: Flow<Boolean> = dataStore.data
        .map { prefs -> prefs[SettingKeys.ifSound] ?: false }

    suspend fun setPomodoroMode(duration: Int) {
        dataStore.edit { prefs -> prefs[SettingKeys.pomodoroMode] = duration }
    }

    suspend fun setShortBreakMode(duration: Int) {
        dataStore.edit { prefs -> prefs[SettingKeys.shortBreakMode] = duration }
    }

    suspend fun setLongBreakMode(duration: Int) {
        dataStore.edit { prefs -> prefs[SettingKeys.longBreakMode] = duration }
    }

    suspend fun setIfNotification(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[SettingKeys.ifNotification] = enabled }
    }

    suspend fun setIfSound(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[SettingKeys.ifSound] = enabled }
    }

}