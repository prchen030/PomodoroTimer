package com.example.pomodorotimer

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedDataViewModel(context: Context) : ViewModel(){

    private val prefs = context.getSharedPreferences("my_prefs",Context.MODE_PRIVATE)
    private val _pomodoroTime =
        MutableStateFlow(prefs.getInt(PrefKeys.KEY_POMODORO_TIME, R.integer.default_duration))
    private val _shortBreakTime =
        MutableStateFlow(prefs.getInt(PrefKeys.KEY_SHORT_BREAK_TIME, R.integer.default_short_break))
    private val _longBreakTime =
        MutableStateFlow(prefs.getInt(PrefKeys.KEY_LONG_BREAK_TIME, R.integer.default_short_break))
    val pomodoroTime: StateFlow<Int> = _pomodoroTime
    val shortBreakTime: StateFlow<Int> = _shortBreakTime
    val longBreakTime: StateFlow<Int> = _longBreakTime

    private val _ifNotification =
        MutableStateFlow(prefs.getBoolean(PrefKeys.KEY_IF_NOTIFICATION, false))
    private val _ifSound =
        MutableStateFlow(prefs.getBoolean(PrefKeys.KEY_IF_SOUND, false))
    val ifNotification: StateFlow<Boolean> = _ifNotification
    val ifSound: StateFlow<Boolean> = _ifSound

    fun getIntValueByKey(key: String): Int {
        return when(key){
            PrefKeys.KEY_POMODORO_TIME -> pomodoroTime.value
            PrefKeys.KEY_SHORT_BREAK_TIME -> shortBreakTime.value
            PrefKeys.KEY_LONG_BREAK_TIME -> longBreakTime.value
            else -> 0
        }
    }

    fun updateIntValue(key: String, value: Int){
        viewModelScope.launch {
            prefs.edit { putInt(key, value)}
            when(key){
                PrefKeys.KEY_POMODORO_TIME -> _pomodoroTime.value = value
                PrefKeys.KEY_SHORT_BREAK_TIME -> _shortBreakTime.value = value
                PrefKeys.KEY_LONG_BREAK_TIME -> _longBreakTime.value = value
            }
        }
    }

    fun updateBooleanValue(key: String, value: Boolean){
        viewModelScope.launch {
            prefs.edit {
                putBoolean(key, value)
            }
            when(key){
                PrefKeys.KEY_IF_NOTIFICATION -> _ifNotification.value = value
                PrefKeys.KEY_IF_SOUND -> _ifSound.value = value
            }
        }
    }

}


