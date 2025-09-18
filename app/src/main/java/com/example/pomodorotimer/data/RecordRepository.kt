package com.example.pomodorotimer.data

class RecordRepository(private val recordDao: RecordDao){

    suspend fun insertRecord(duration: Double, date: String){
        val record = Record(duration = duration, date = date)
        recordDao.insert(record)
    }

    suspend fun getRecordsWithinDays(startDate: String, endDate: String) = recordDao.getRecordsWithinDays(startDate, endDate)

    suspend fun getRecordsWithinMonths(startMonth: String, endMonth: String) = recordDao.getRecordsWithinMonths(startMonth, endMonth)

    suspend fun getRecordsWithinYears(startYear: String, endYear: String) = recordDao.getRecordsWithinYears(startYear, endYear)

}