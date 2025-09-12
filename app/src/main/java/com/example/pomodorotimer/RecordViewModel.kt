package com.example.pomodorotimer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodorotimer.data.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val recordRepository: RecordRepository
) : ViewModel(){

    private val _recordsWithinDays = MutableLiveData<List<com.example.pomodorotimer.data.Record?>>()
    val recordsWithDays : LiveData<List<com.example.pomodorotimer.data.Record?>> = _recordsWithinDays

    private val _recordsWithinMonths = MutableLiveData<List<com.example.pomodorotimer.data.Record?>>()
    val recordsWithMonths : LiveData<List<com.example.pomodorotimer.data.Record?>> = _recordsWithinMonths

    private val _recordsWithinYears = MutableLiveData<List<com.example.pomodorotimer.data.Record?>>()
    val recordsWithYears : LiveData<List<com.example.pomodorotimer.data.Record?>> = _recordsWithinYears

    suspend fun insertRecord( duration: Double, date: String): Int = recordRepository.insertRecord(duration, date)

    suspend fun updateRecord(record: Record) = recordRepository.updateRecord(record)

    suspend fun getRecordById(id: Long) = recordRepository.getRecordById(id)

    fun getRecordsWithinDays(date: LocalDate){
        val list = getConsecutiveDays(date)
        viewModelScope.launch(Dispatchers.IO) {
            _recordsWithinDays.postValue(recordRepository.getRecordsWithinDays(list.first(), list.last()))
        }
    }

    fun getRecordsWithinMonths(date: LocalDate){
        val list = getConsecutiveMonths(date.year, date.month)
        val startMonth = Month.valueOf(list.first().uppercase()).value.toString()
        val endMonth = Month.valueOf(list.last().uppercase()).value.toString()
        viewModelScope.launch(Dispatchers.IO) {
            _recordsWithinMonths.postValue(recordRepository.getRecordsWithinMonths(startMonth, endMonth))
        }
    }

    fun getRecordsWithinYears(date: LocalDate){
        val list = getConsecutiveYears(date.year)
        viewModelScope.launch(Dispatchers.IO) {
            _recordsWithinYears.postValue(recordRepository.getRecordsWithinYears(list.first(), list.last()))
        }
    }

    private fun getConsecutiveYears(startYear: Int): List<String> {
        return (-1..1).map { offset -> (startYear + offset).toString() }
    }

    private fun getConsecutiveMonths(year: Int, month: Month): List<String>{
        val startMonth = YearMonth.of(year, month)
        return (-2..2).map { offset ->
            startMonth.plusMonths(offset.toLong())
                .month
                .getDisplayName(java.time.format.TextStyle.SHORT, Locale.ENGLISH)
        }
    }

    private fun getConsecutiveDays(startDate: LocalDate): List<String>{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return (0..6).map { offset ->
            startDate.minusDays(offset.toLong()).format(formatter)
        }
    }

}