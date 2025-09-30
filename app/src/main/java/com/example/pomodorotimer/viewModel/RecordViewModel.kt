package com.example.pomodorotimer.viewModel

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodorotimer.model.ChartViewMode
import com.example.pomodorotimer.R
import com.example.pomodorotimer.model.TimerStates
import com.example.pomodorotimer.data.RecordRepository
import com.example.pomodorotimer.data.SettingRepository
import com.example.pomodorotimer.model.showNotification
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RecordViewModel (
    private val recordRepository: RecordRepository,
    private val settingRepository: SettingRepository
) : ViewModel(){

    val pomodoroTime = settingRepository.pomodoroMode.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        TimerStates.POMODORO.default
    )

    val shortBreakTime = settingRepository.shortBreakMode.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        TimerStates.SHORT_BREAK.default
    )

    val longBreakTime = settingRepository.longBreakMode.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        TimerStates.LONG_BREAK.default
    )

    private val _timeLeft = MutableStateFlow(0)
    val timeLeft : StateFlow<Int> = _timeLeft
    private var job: Job? = null

    private val _chartViewMode = MutableStateFlow(ChartViewMode.WEEK)
    val chartViewMode: StateFlow<ChartViewMode> = _chartViewMode.asStateFlow()

    private val _xAxisData = MutableStateFlow<List<String>>(emptyList())
    val xAxisData : StateFlow<List<String>> = _xAxisData

    private val _yAxisData = MutableStateFlow<List<Double>>(emptyList())
    val yAxisData : StateFlow<List<Double>> = _yAxisData

    private val _isBreak = MutableStateFlow(false)

    private val _durationCount = MutableStateFlow(0)

    private val _state = MutableStateFlow(TimerStates.POMODORO)
    val state : StateFlow<TimerStates> = _state

    private val _isRunning = MutableStateFlow(false)
    val isRunning : StateFlow<Boolean> = _isRunning

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val today = LocalDate.now().format(formatter)

    init {
        viewModelScope.launch {
            setAxisDataList(_chartViewMode.value, LocalDate.now())

            combine(_state, pomodoroTime, shortBreakTime, longBreakTime, _isRunning)
            { state, pomo, short, long, isRunning ->
                if (!isRunning) {
                    when (state) {
                        TimerStates.POMODORO -> pomo * MINUTE_SECONDS
                        TimerStates.SHORT_BREAK -> short * MINUTE_SECONDS
                        TimerStates.LONG_BREAK -> long * MINUTE_SECONDS
                    }
                } else {
                    _timeLeft.value
                }
            }.collect { newTime ->
                _timeLeft.value = newTime
            }
        }
    }

    fun insertRecord(
        duration: Double,
        date: String
    ) = viewModelScope.launch {
        recordRepository.insertRecord(duration, date)
    }

    private fun setState() {
        _state.value = if (!_isBreak.value) {
            TimerStates.POMODORO
        } else if (_durationCount.value % 4 == 0) {
            TimerStates.LONG_BREAK
        } else {
            TimerStates.SHORT_BREAK
        }
    }

    fun startCountdown(context: Context){
        job?.cancel()
        job = viewModelScope.launch {
            if(!_isRunning.value){
                _isRunning.value = true
                val mins = (_timeLeft.value / MINUTE_SECONDS).toDouble()
                while (_timeLeft.value > 0) {
                    delay(1000)
                    _timeLeft.value -= 1
                }
                if(!_isBreak.value){
                    _durationCount.value++
                    insertRecord(mins, today)
                }
                _isBreak.value = !_isBreak.value
                _isRunning.value = false
                setState()
                sendNotification(context, _isBreak.value)
            }
        }
    }

    fun pauseCountdown(){
        _isRunning.value = false
        job?.cancel()
    }

    fun cancelCountdown(){
        job?.cancel()
        job = null
    }

    fun setChartViewMode(mode: ChartViewMode, date: LocalDate) {
        viewModelScope.launch {
            _chartViewMode.value = mode
            setAxisDataList(mode, date)
        }
    }

    private suspend fun setAxisDataList(mode: ChartViewMode, date: LocalDate){
        val newXList = mutableListOf<String>()
        val newYList = mutableListOf<Double>()
         when(mode){
            ChartViewMode.WEEK->{
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                (-3..3).map { offset ->
                    val newDate = date.plusDays(offset.toLong()).format(formatter)
                    val dateTotal = recordRepository.getRecordByDay(newDate)
                    newXList.add(newDate.substring(6,10))
                    newYList.add(dateTotal.total)
                }
            }
             ChartViewMode.MONTH->{
                 val formatter = DateTimeFormatter.ofPattern("yyyy-MM")
                 (-2..2).map { offset ->
                     val newMonth = LocalDate.from(date).plusMonths(offset.toLong()).format(formatter)
                     val yearMonthTotal = recordRepository.getRecordByMonth(newMonth)
                     newXList.add(newMonth)
                     newYList.add(yearMonthTotal.total)
                 }
             }
             ChartViewMode.YEAR->{
                 val year = date.year
                 (-2..2).map { offset ->
                     val newYear = (year + offset).toString()
                     val yearTotal = recordRepository.getRecordByYear(newYear)
                     newXList.add(newYear)
                     newYList.add(yearTotal.total)
                 }
             }
        }
        _xAxisData.value = newXList
        _yAxisData.value = newYList
    }

    private fun sendNotification(context: Context, isBreak: Boolean){
        if(isBreak){
            showNotification(
                context,
                ContextCompat.getString(context, R.string.notification_message_break)
            )
        }else{
            showNotification(
                context,
                ContextCompat.getString(context, R.string.notification_message_new)
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelCountdown()
    }
}

const val MINUTE_SECONDS = 60