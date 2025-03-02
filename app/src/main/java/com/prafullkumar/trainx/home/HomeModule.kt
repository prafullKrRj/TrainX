package com.prafullkumar.trainx.home

import androidx.room.Room
import com.prafullkumar.trainx.home.data.HomeRepository
import com.prafullkumar.trainx.home.data.local.HydrationDao
import com.prafullkumar.trainx.home.data.local.HydrationDatabase
import com.prafullkumar.trainx.home.data.local.WaterQuantitySharedPref
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {

    single<HydrationDatabase> {
        Room.databaseBuilder(androidContext(), HydrationDatabase::class.java, "hydration_database")
            .build()
    }
    single<HydrationDao> {
        get<HydrationDatabase>().hydrationDao()
    }
    single {
        WaterQuantitySharedPref(androidContext())
    }
    single {
        HomeRepository(get(), get(), get(), get(), get())
    }
    viewModel {
        HomeViewModel(get())
    }
}