package com.example.pomodorotimer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecordDao {

    @Insert
    suspend fun insert(record: Record): Int

    @Query("SELECT * FROM records WHERE date BETWEEN :startDate AND :endDate GROUP BY date")
    fun getRecordsWithinDays(startDate: String, endDate: String): List<Record?>

    @Query("SELECT * FROM records WHERE Month(date) BETWEEN :startMonth AND :endMonth GROUP BY Month(date)")
    fun getRecordsWithinMonths(startMonth: String, endMonth: String): List<Record?>

    @Query("SELECT * FROM records WHERE Year(date) BETWEEN :startYear AND :endYear GROUP BY Year(date)")
    fun getRecordsWithinYears(startYear: String, endYear: String): List<Record?>

}