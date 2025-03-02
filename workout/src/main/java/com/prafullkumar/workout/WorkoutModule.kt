package com.prafullkumar.workout

import androidx.room.Room
import com.prafullkumar.workout.data.PopulatingRepository
import com.prafullkumar.workout.data.WorkoutScreenRepository
import com.prafullkumar.workout.data.local.TrainXDatabase
import com.prafullkumar.workout.logging.data.WorkoutLoggingRepository
import com.prafullkumar.workout.logging.data.local.UserWorkoutDao
import com.prafullkumar.workout.logging.data.local.UserWorkoutsDB
import com.prafullkumar.workout.logging.ui.WorkoutLoggingViewModel
import com.prafullkumar.workout.ui.WorkoutViewModel
import com.prafullkumar.workout.ui.planDetailScreen.PlanDetailViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val workoutModule = module {
    single {
        Room.databaseBuilder(androidContext(), TrainXDatabase::class.java, "exercise-database")
            .build()
    }
    single {
        get<TrainXDatabase>().equipmentDao()
    }
    single {
        get<TrainXDatabase>().exerciseDao()
    }
    single {
        get<TrainXDatabase>().workoutPlanDao()
    }
    single { PopulatingRepository() }
    single {
        WorkoutScreenRepository()
    }
    viewModel { WorkoutViewModel(get()) }
    viewModel {
        com.prafullkumar.workout.ui.customPlans.CreatePlanViewModel(
            get(),
            androidContext()
        )
    }
    viewModel { PlanDetailViewModel(get(), get()) }

    single<UserWorkoutsDB> {
        Room.databaseBuilder(androidContext(), UserWorkoutsDB::class.java, "user_workouts")
            .build()
    }
    single<UserWorkoutDao> { get<UserWorkoutsDB>().userWorkoutDao() }
    single { WorkoutLoggingRepository(workoutLoggingDao = get(), get()) }
    viewModel { WorkoutLoggingViewModel(get(), get()) }
}