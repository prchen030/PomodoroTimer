package com.example.pomodorotimer.data

import android.app.Application
import androidx.room.Room
import com.example.pomodorotimer.RecordViewModel
import com.example.pomodorotimer.SharedDataViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            get<Application>(),
            AppDatabase::class.java,
            "app_db"
        ).build()
    }

    single { get<AppDatabase>().recordDao() }
    single { RecordRepository(get()) }
    single { SharedDataViewModel(get()) }
    viewModel {
        RecordViewModel(
            recordRepository = get(),
            sharedDataViewModel = get()
        )
    }

}