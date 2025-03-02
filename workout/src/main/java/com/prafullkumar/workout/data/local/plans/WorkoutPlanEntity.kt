package com.prafullkumar.workout.data.local.plans

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prafullkumar.workout.data.ExperienceLevel
import com.prafullkumar.workout.data.PlanCategory
import com.prafullkumar.workout.data.PlanType
import com.prafullkumar.workout.data.WeightReps
import com.prafullkumar.workout.data.WorkoutExercise
import com.prafullkumar.workout.data.WorkoutPlan
import com.prafullkumar.workout.data.WorkoutSplit
import com.prafullkumar.workout.logging.data.local.CompletionStatus

@Entity(tableName = "workout_plans")
data class WorkoutPlanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val frequency: Int, // days per week
    val level: ExperienceLevel,
    val type: PlanType,
    @TypeConverters(SplitsTypeConverter::class)
    val splits: List<WorkoutSplit>,
    val category: String = PlanCategory.PREDEFINED.name,
    val colorValue: Int = 0xFF5D4037.toInt(), // Default color for plans
    val imageUrl: String? = null // Optional background image URL for the plan card
) {
    fun getColor(): Color = Color(colorValue)
    fun toWorkoutPlan() = WorkoutPlan(
        id = id,
        name = name,
        description = description,
        frequency = frequency,
        level = level,
        type = type,
        splits = splits.toMutableList(),
        category = PlanCategory.valueOf(category),
        color = Color(colorValue),
        imageUrl = imageUrl
    )
}

class SetsRepsTypeConverter {
    @TypeConverter
    fun fromSetsRepsList(weightReps: List<WeightReps>): String {
        val gson = Gson()
        val type = object : TypeToken<List<WeightReps>>() {}.type
        return gson.toJson(weightReps, type)
    }

    @TypeConverter
    fun toSetsRepsList(setsRepsString: String): List<WeightReps> {
        val gson = Gson()
        val type = object : TypeToken<List<WeightReps>>() {}.type
        return gson.fromJson(setsRepsString, type)
    }
}

class SplitsTypeConverter {
    @TypeConverter
    fun fromSplitsList(splits: List<WorkoutSplit>): String {
        return Gson().toJson(splits)
    }

    @TypeConverter
    fun toSplitsList(splitsString: String): List<WorkoutSplit> {
        val type = object : TypeToken<List<WorkoutSplit>>() {}.type
        return Gson().fromJson(splitsString, type)
    }
}

class ExercisesTypeConverter {
    @TypeConverter
    fun fromExercisesList(exercises: List<WorkoutExercise>): String {
        return Gson().toJson(exercises)
    }

    @TypeConverter
    fun toExercisesList(exercisesString: String): List<WorkoutExercise> {
        val type = object : TypeToken<List<WorkoutExercise>>() {}.type
        return Gson().fromJson(exercisesString, type)
    }
}

class EnumConverters {
    @TypeConverter
    fun fromExperienceLevel(level: ExperienceLevel): String = level.name

    @TypeConverter
    fun toExperienceLevel(level: String): ExperienceLevel =
        ExperienceLevel.valueOf(level)

    @TypeConverter
    fun fromPlanType(type: PlanType): String = type.name

    @TypeConverter
    fun toPlanType(type: String): PlanType =
        PlanType.valueOf(type)

    @TypeConverter
    fun fromCompletionStatus(status: CompletionStatus) = status.name

    @TypeConverter
    fun toCompletionStatus(status: String): CompletionStatus =
        CompletionStatus.valueOf(status)
}