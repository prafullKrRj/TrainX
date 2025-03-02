package com.prafullkumar.workout.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.prafullkumar.workout.data.local.dao.EquipmentDao
import com.prafullkumar.workout.data.local.dao.ExerciseDao
import com.prafullkumar.workout.data.local.plans.EnumConverters
import com.prafullkumar.workout.data.local.plans.ExercisesTypeConverter
import com.prafullkumar.workout.data.local.plans.SplitsTypeConverter
import com.prafullkumar.workout.data.local.plans.WorkoutPlanEntity
import com.prafullkumar.workout.data.local.plans.WorkoutPlansDao


@Database(
    entities = [ExerciseEntity::class, com.prafullkumar.workout.data.local.EquipmentEntity::class, WorkoutPlanEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    StringListConverter::class,
    SplitsTypeConverter::class,
    ExercisesTypeConverter::class,
    EnumConverters::class
)
abstract class TrainXDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun equipmentDao(): EquipmentDao
    abstract fun workoutPlanDao(): WorkoutPlansDao
}