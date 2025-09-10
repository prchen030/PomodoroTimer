package com.example.pomodorotimer.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName="records")
data class Record(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val duration: Int,
    val date: Date
)
