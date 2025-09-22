package com.example.pomodorotimer.data

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.pomodorotimer.viewModel.RecordViewModel
import com.example.pomodorotimer.viewModel.SettingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
val appModule = module {
    single {
        Room.databaseBuilder(
            get<Application>(),
            AppDatabase::class.java,
            "app_db"
        ).build()
    }

    single<DataStore<Preferences>> { get<Context>().dataStore }
    single { get<AppDatabase>().recordDao() }
    single { RecordRepository(recordDao = get()) }
    single { SettingRepository(dataStore = get()) }
    viewModel {
        SettingViewModel(settingRepository = get())
    }
    viewModel {
        RecordViewModel(
            recordRepository = get(),
            settingRepository = get()
        )
    }
}