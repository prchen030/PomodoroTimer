package com.example.pomodorotimer.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [Record::class])
abstract class AppDatabase : RoomDatabase(){
    abstract fun recordDao(): RecordDao
}