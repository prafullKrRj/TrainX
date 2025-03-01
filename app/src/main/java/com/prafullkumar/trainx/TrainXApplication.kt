package com.prafullkumar.trainx

import android.app.Application
import com.prafullkumar.common.commonModule
import com.prafullkumar.foodlog.foodLogModule
import com.prafullkumar.onboarding.onBoardingModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TrainXApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TrainXApplication)
            androidLogger()
            modules(commonModule, onBoardingModule, foodLogModule)
        }
    }
}