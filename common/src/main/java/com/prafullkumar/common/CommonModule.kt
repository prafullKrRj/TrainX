package com.prafullkumar.common

import androidx.room.Room
import com.prafullkumar.common.data.local.UserDataDao
import com.prafullkumar.common.data.local.UserDataDatabase
import com.prafullkumar.common.utils.SharedPrefManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val commonModule = module {
    single<UserDataDatabase> {
        Room.databaseBuilder(
            get(),
            UserDataDatabase::class.java,
            "user_data_database"
        ).build()
    }
    single<UserDataDao> { get<UserDataDatabase>().userDataEntityDao() }
    single<SharedPrefManager> { SharedPrefManager(androidContext()) }
}