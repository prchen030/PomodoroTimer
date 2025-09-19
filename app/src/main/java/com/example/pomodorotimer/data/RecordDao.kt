package com.example.pomodorotimer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecordDao {

    @Insert
    suspend fun insert(record: Record)

    @Query("""
        SELECT :date AS date,
            IFNULL(SUM(duration), 0.0) AS total 
        FROM records 
        WHERE date = :date
    """)
    suspend fun getRecordByDay(date: String): DateTotal

    @Query("""
        SELECT CAST(substr(`date`, 1, 4) AS INTEGER) AS year,
           CAST(substr(`date`, 6, 2) AS INTEGER) AS month,
           IFNULL(SUM(duration), 0.0) AS total
        FROM records
        WHERE substr(date, 1, 7) = :month
    """)
    suspend fun getRecordByMonth(month: String): YearMonthTotal

    @Query("""
        SELECT :year AS year,
            IFNULL(SUM(duration), 0.0) AS total
        FROM records
        WHERE substr(`date`, 1, 4) = :year
    """)
    suspend fun getRecordByYear(year: String): YearTotal

}