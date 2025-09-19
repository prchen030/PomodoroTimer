package com.example.pomodorotimer.viewModel

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodorotimer.units.ChartViewMode
import com.example.pomodorotimer.units.PrefKeys
import com.example.pomodorotimer.R
import com.example.pomodorotimer.data.AxisData
import com.example.pomodorotimer.units.TimerStates
import com.example.pomodorotimer.data.Record
import com.example.pomodorotimer.data.RecordRepository
import com.example.pomodorotimer.data.YValueProvider
import com.example.pomodorotimer.data.YearMonthTotal
import com.example.pomodorotimer.units.showNotification
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RecordViewModel (
    private val recordRepository: RecordRepository,
    private val sharedDataViewModel: SharedDataViewModel
) : ViewModel(){

    private val _timeLeft = MutableStateFlow(0)
    val timeLeft : StateFlow<Int> = _timeLeft
    private var job: Job? = null

    private val _chartViewMode = MutableStateFlow(ChartViewMode.WEEK)
    val chartViewMode: StateFlow<ChartViewMode> = _chartViewMode.asStateFlow()

    private val _historicalData = MutableStateFlow<List<AxisData>>(emptyList())
    val historicalData : StateFlow<List<AxisData>> = _historicalData

    private val _isRunning = MutableStateFlow(false)
    val isRunning : StateFlow<Boolean> = _isRunning

    private val _isBreak = MutableStateFlow(false)

    private val _durationCount = MutableStateFlow(0)

    private val _state = MutableStateFlow(TimerStates.POMODORO)
    val state : StateFlow<TimerStates> = _state

    private var isPause = false

    init {
        _timeLeft.value = sharedDataViewModel.pomodoroTime.value * MINUTE_SECONDS
        setChartViewMode(_chartViewMode.value, LocalDate.now())
    }

    suspend fun insertRecord( duration: Double, date: String) = recordRepository.insertRecord(duration, date)

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

        if(!_isRunning.value){
            _isRunning.value = true
            var key = setKey(_state.value)
            val mins = sharedDataViewModel.getIntValueByKey(key)
            if(!isPause) {
                job?.cancel()
                _timeLeft.value = mins * MINUTE_SECONDS
            }
            val duration = _timeLeft.value

            job = viewModelScope.launch {
                for (i in duration downTo 0) {
                    _timeLeft.value = i
                    delay(1000)
                }
                if(!_isBreak.value){
                    _durationCount.value++
                    insertRecord(mins.toDouble(), date)
                }

                Log.i(
                    "RecordViewModel",
                    "${state.value}: ${_durationCount.value} Pomodoro"
                )
                _isBreak.value = !_isBreak.value
                _isRunning.value = false
                setState()
                key = setKey(_state.value)
                _timeLeft.value = sharedDataViewModel.getIntValueByKey(key) * MINUTE_SECONDS

                sendNotification(context, _isBreak.value)
            }
        }
    }

    fun pauseCountdown(){
        _isRunning.value = false
        isPause = true
    }

    fun cancelCountdown(){
        job?.cancel()
        job = null
    }

    fun setChartViewMode(mode: ChartViewMode, date: LocalDate) {
        viewModelScope.launch {
            _chartViewMode.value = mode
            _historicalData.value = getAxisDataList(mode, date)
        }
    }

    suspend fun getAxisDataList(mode: ChartViewMode, date: LocalDate): List<AxisData>{
        val list = mutableListOf<AxisData>()
         when(mode){
            ChartViewMode.WEEK->{
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                (-3..3).map { offset ->
                    val newDate = date.plusDays(offset.toLong()).format(formatter)
                    val dateTotal = recordRepository.getRecordByDay(newDate)
                    list.add(AxisData(newDate.substring(6,10), dateTotal.yValue))
                }
            }
             ChartViewMode.MONTH->{
                 val formatter = DateTimeFormatter.ofPattern("yyyy-MM")
                 (-2..2).map { offset ->
                     val newMonth = LocalDate.from(date).plusMonths(offset.toLong()).format(formatter)
                     val yearMonthTotal = recordRepository.getRecordByMonth(newMonth)
                     list.add(AxisData(newMonth, yearMonthTotal.yValue))
                 }
             }
             ChartViewMode.YEAR->{
                 val year = date.year
                 (-2..2).map { offset ->
                     val newYear = (year + offset).toString()
                     val yearTotal = recordRepository.getRecordByYear(newYear)
                     list.add(AxisData(newYear, yearTotal.yValue))
                 }
             }
        }
        return  list
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