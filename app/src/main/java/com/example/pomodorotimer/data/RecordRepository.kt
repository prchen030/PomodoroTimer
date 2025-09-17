package com.example.pomodorotimer.data

import javax.inject.Inject

class RecordRepository @Inject constructor(
    private val recordDao: RecordDao,
){

    suspend fun insertRecord(duration: Double, date: String): Int{
        val record = Record(duration = duration, date = date)
        return recordDao.insert(record)
    }

    fun getRecordsWithinDays(startDate: String, endDate: String) = recordDao.getRecordsWithinDays(startDate, endDate)

    fun getRecordsWithinMonths(startMonth: String, endMonth: String) = recordDao.getRecordsWithinMonths(startMonth, endMonth)

    fun getRecordsWithinYears(startYear: String, endYear: String) = recordDao.getRecordsWithinYears(startYear, endYear)

}