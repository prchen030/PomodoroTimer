package com.example.pomodorotimer.data

class RecordRepository(private val recordDao: RecordDao){

    suspend fun insertRecord(duration: Double, date: String){
        val record = Record(duration = duration, date = date)
        recordDao.insert(record)
    }

    suspend fun getRecordByDay(date: String) = recordDao.getRecordByDay(date)

    suspend fun getRecordByMonth(month: String) = recordDao.getRecordByMonth(month)

    suspend fun getRecordByYear(year: String) = recordDao.getRecordByYear(year)

}