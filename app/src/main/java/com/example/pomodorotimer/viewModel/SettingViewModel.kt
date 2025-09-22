package com.example.pomodorotimer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodorotimer.data.SettingRepository
import com.example.pomodorotimer.units.PrefKeys
import com.example.pomodorotimer.units.TimerStates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingViewModel(
    private val settingRepository: SettingRepository
) : ViewModel(){
    val pomodoroTime: StateFlow<Int> = settingRepository.pomodoroMode
        .stateIn(viewModelScope, SharingStarted.Eagerly,
        TimerStates.POMODORO.default)

    val shortBreakTime: StateFlow<Int> = settingRepository.shortBreakMode.stateIn(viewModelScope, SharingStarted.Eagerly,
        TimerStates.SHORT_BREAK.default)

    val longBreakTime: StateFlow<Int> = settingRepository.longBreakMode.stateIn(viewModelScope, SharingStarted.Eagerly,
        TimerStates.LONG_BREAK.default)

    val ifNotification: StateFlow<Boolean> = settingRepository.ifNotification.stateIn(viewModelScope, SharingStarted.Eagerly,
        false)

    val ifSound: StateFlow<Boolean> = settingRepository.ifSound.stateIn(viewModelScope, SharingStarted.Eagerly,
        false)

    private val _isGranted = MutableStateFlow(false)
    val isGranted : StateFlow<Boolean> = _isGranted

    fun setGrantValue(){
        _isGranted.value = true
    }

    fun updateIntValue(key: String, value: Int){
        viewModelScope.launch {
            when(key){
                PrefKeys.KEY_POMODORO_TIME -> settingRepository.setPomodoroMode(value)
                PrefKeys.KEY_SHORT_BREAK_TIME -> settingRepository.setShortBreakMode(value)
                PrefKeys.KEY_LONG_BREAK_TIME -> settingRepository.setLongBreakMode(value)
            }
        }
    }

    fun updateBooleanValue(key: String, value: Boolean) {
        viewModelScope.launch {
            when (key) {
                PrefKeys.KEY_IF_NOTIFICATION -> settingRepository.setIfNotification(value)
                PrefKeys.KEY_IF_SOUND -> settingRepository.setIfSound(value)
            }
        }
    }

}