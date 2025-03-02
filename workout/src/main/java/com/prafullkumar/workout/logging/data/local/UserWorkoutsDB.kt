package com.prafullkumar.workout.logging.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.prafullkumar.workout.data.local.plans.EnumConverters

@Database(
    entities = [UserWorkoutEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    LoggedExerciseTypeConverter::class,
    DateTimeConverter::class,
    EnumConverters::class
)
abstract class UserWorkoutsDB : RoomDatabase() {
    abstract fun userWorkoutDao(): UserWorkoutDao
}