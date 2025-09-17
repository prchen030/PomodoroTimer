package com.example.pomodorotimer

import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodorotimer.data.RecordRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class RecordViewModel (
    private val recordRepository: RecordRepository,
    private val sharedDataViewModel: SharedDataViewModel
) : ViewModel(){

    private val _timeLeft = MutableStateFlow(0)
    val timeLeft : StateFlow<Int> = _timeLeft
    private var job: Job? = null

    private val _chartViewMode = MutableStateFlow(ChartViewMode.WEEK)
    val chartViewMode: StateFlow<ChartViewMode> = _chartViewMode

    private val _historicalData = MutableStateFlow<List<com.example.pomodorotimer.data.Record?>>(emptyList())
    val historicalData : StateFlow<List<com.example.pomodorotimer.data.Record?>> = _historicalData

    private val _isBreak = MutableStateFlow(false)

    private val _durationCount =  MutableStateFlow(0)

    private val _state =  MutableStateFlow(TimerStates.POMODORO)
    val state : StateFlow<TimerStates> = _state

    suspend fun insertRecord( duration: Double, date: String): Int = recordRepository.insertRecord(duration, date)

    fun setState(){
        return if(!_isBreak.value){
            _state.value = TimerStates.POMODORO
        }else{
            if(_durationCount.value%4 == 0){
                _state.value = TimerStates.LONG_BREAK
            }else{
                _state.value = TimerStates.SHORT_BREAK
            }
        }
    }

    private fun setKey(state: TimerStates) = when(state){
        TimerStates.SHORT_BREAK -> PrefKeys.KEY_SHORT_BREAK_TIME
        TimerStates.LONG_BREAK -> PrefKeys.KEY_LONG_BREAK_TIME
        else -> PrefKeys.KEY_POMODORO_TIME
    }

    fun startCountdown(date: String, context: Context) {
        job?.cancel()
        val key = setKey(_state.value)
        val duration = sharedDataViewModel.getIntValueByKey(key) * 60
        job = viewModelScope.launch {
            for (i in duration downTo 0) {
                _timeLeft.value = i
                delay(1000)
            }
            if(!_isBreak.value){
                insertRecord(duration.toDouble(), date)
            }
            _isBreak.value = !_isBreak.value
            _durationCount.value++
            setState()
            sendNotification(context, _isBreak.value)
        }
    }

    fun pauseCountdown(){
        job?.cancel()
    }

    fun setChartViewMode(mode: ChartViewMode) {
        _chartViewMode.value = mode
    }

    fun fetchHistoricalData(mode: ChartViewMode, xList: List<String>){
        viewModelScope.launch {
             when (mode) {
                ChartViewMode.WEEK -> recordRepository.getRecordsWithinDays(xList.first(), xList.last())
                ChartViewMode.MONTH -> recordRepository.getRecordsWithinMonths(xList.first(), xList.last())
                ChartViewMode.YEAR -> recordRepository.getRecordsWithinYears(xList.first(), xList.last())
            }
        }
    }

    fun getXAxisData(mode: ChartViewMode, date: LocalDate): List<String> {
        val year = date.year
        val month = date.month
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return when (mode) {
            ChartViewMode.WEEK -> {
                (0..6).map { offset ->
                    date.minusDays(offset.toLong()).format(formatter).toString()
                }.toList()
            }

            ChartViewMode.MONTH -> {
                val startMonth = YearMonth.of(year, month)
                (-2..2).map { offset ->
                    startMonth.plusMonths(offset.toLong()).month.value.toString()
                }.toList()
            }

            ChartViewMode.YEAR -> {
                (-1..1).map { offset -> (year + offset).toString() }.toList()
            }
        }
    }

    private fun sendNotification(context: Context, isBreak: Boolean){
        if(isBreak){
            showNotification(context, getString(context, R.string.notification_message_break))
        }else{
            showNotification(context, getString(context, R.string.notification_message_new))
        }
    }

}