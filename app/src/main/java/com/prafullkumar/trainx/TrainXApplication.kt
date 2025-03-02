package com.prafullkumar.trainx

import android.app.Application
import com.prafullkumar.common.commonModule
import com.prafullkumar.foodlog.foodLogModule
import com.prafullkumar.onboarding.onBoardingModule
import com.prafullkumar.profile.profileModule
import com.prafullkumar.trainx.home.homeModule
import com.prafullkumar.trainxai.aiModule
import com.prafullkumar.workout.data.PopulatingRepository
import com.prafullkumar.workout.workoutModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.get
import org.koin.core.context.startKoin
import org.koin.dsl.module

class DatabasePopulator(
    private val repository: PopulatingRepository
) {
    suspend fun populateIfEmpty() {
        repository.populateDatabase()
    }
}

val populatorModule = module {
    single { DatabasePopulator(get()) }
}

class TrainXApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TrainXApplication)
            modules(
                commonModule,
                homeModule,
                onBoardingModule,
                workoutModule,
                populatorModule,
                foodLogModule,
                profileModule,
                aiModule
            )
        }
        CoroutineScope(Dispatchers.IO).launch {
            val populator = get().get<DatabasePopulator>()
            populator.populateIfEmpty()
        }
    }
}