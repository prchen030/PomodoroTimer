package com.example.pomodorotimer.data

import java.util.Date
import javax.inject.Inject

class RecordRepository @Inject constructor(
    private val recordDao: RecordDao,
){
    val allRecords = recordDao.getAllRecords()

    suspend fun getRecordById(id: Long) = recordDao.getRecordById(id)

    suspend fun insertRecord(record: Record) = recordDao.insert(record)

    suspend fun updateRecord(record: Record) = recordDao.update(record)

    fun getRecordsByWeek(date: Date) = recordDao.getRecordsByWeek(date)

    fun getRecordsByMonth(month: Int) = recordDao.getRecordsByMonth(month)

    fun getRecordsByYear(year: Int) = recordDao.getRecordsByYear(year)

}