package com.example.pomodorotimer.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.Date

@Dao
interface RecordDao {

    @Insert
    suspend fun insert(record: Record)

    @Update
    suspend fun update(record: Record)

    @Delete
    suspend fun delete(record: Record)

    @Query("SELECT * FROM records WHERE id = :id")
    suspend fun getRecordById(id: Long): Record

    @Query("SELECT * FROM records")
    fun getAllRecords(): LiveData<List<Record>>

    @Query("SELECT * FROM records WHERE datepart('ww',date) = Week(:day)")
    fun getRecordsByWeek(day: Date)

    @Query("SELECT * FROM records WHERE Month(date) = :month")
    fun getRecordsByMonth(month: Int)

    @Query("SELECT * FROM records WHERE Year(date) = :year")
    fun getRecordsByYear(year: Int)

}