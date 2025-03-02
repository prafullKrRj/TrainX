package com.prafullkumar.workout.logging.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

@Entity(tableName = "user_workouts")
data class UserWorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val planId: Long?, // Can be null for custom/one-off workouts
    val splitName: String, // e.g., "Push Day", "Legs", etc.
    @TypeConverters(DateTimeConverter::class)
    val date: Date = Date(), // When the workout was performed
    val duration: Int = 0, // Duration in minutes
    @TypeConverters(LoggedExerciseTypeConverter::class)
    val exercises: List<LoggedExercise> = emptyList(),
    val completionStatus: CompletionStatus = CompletionStatus.COMPLETED,
    val notes: String? = null,
    val caloriesBurned: Int = 0,
    val rating: Int? = null, // User rating of workout (1-5)
    val feelingBefore: String? = null, // Optional user mood/feeling before workout
    val feelingAfter: String? = null // Optional user mood/feeling after workout,
)

// User workout logging entities
enum class CompletionStatus {
    COMPLETED, PARTIAL, SKIPPED
}

data class LoggedExerciseSet(
    val setNumber: Int,
    val weight: Float?, // null if bodyweight exercise
    val reps: Int,
    val isCompleted: Boolean = true,
    val rpe: Int? = null, // Rate of Perceived Exertion (1-10)
    val notes: String? = null
)

data class LoggedExercise(
    val exerciseId: Long,
    val exerciseName: String,
    val sets: List<LoggedExerciseSet>,
    val notes: String? = null,
    val completionStatus: CompletionStatus = CompletionStatus.COMPLETED
)

class LoggedExerciseTypeConverter {
    @TypeConverter
    fun fromLoggedExerciseList(exercises: List<LoggedExercise>): String {
        return Gson().toJson(exercises)
    }

    @TypeConverter
    fun toLoggedExerciseList(exercisesString: String): List<LoggedExercise> {
        val type = object : TypeToken<List<LoggedExercise>>() {}.type
        return Gson().fromJson(exercisesString, type)
    }
}

class DateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): java.util.Date? {
        return value?.let { java.util.Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: java.util.Date?): Long? {
        return date?.time
    }
}