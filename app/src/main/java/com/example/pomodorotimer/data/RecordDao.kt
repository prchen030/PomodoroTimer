package com.example.pomodorotimer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.jetbrains.annotations.NotNull

@Dao
interface RecordDao {

    @Insert
    suspend fun insert(record: Record)

    @Query("""
        SELECT date as date, 
            SUM(duration) AS total 
        FROM records 
        WHERE date BETWEEN :startDate AND :endDate 
        GROUP BY date
    """)
    suspend fun getRecordsWithinDays(startDate: String, endDate: String): List<DateTotal>?

    @Query("""
        SELECT CAST(substr(date, 1, 4) AS INTEGER) AS year,
           CAST(substr(date, 6, 2) AS INTEGER) AS month,
           SUM(duration) AS total
        FROM records
        WHERE substr(date, 1, 7) BETWEEN :startMonth AND :endMonth
        GROUP BY year, month
        ORDER BY year, month
    """)
    suspend fun getRecordsWithinMonths(startMonth: String, endMonth: String): List<YearMonthTotal>?

    @Query("""
        SELECT CAST(substr(date, 1, 4) AS INTEGER) AS year,
            SUM(duration) AS total
        FROM records
        WHERE year BETWEEN :startYear AND :endYear
        GROUP BY year
        ORDER BY year
    """)
    suspend fun getRecordsWithinYears(startYear: String, endYear: String): List<YearTotal>?

}