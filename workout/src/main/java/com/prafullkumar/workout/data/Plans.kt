package com.prafullkumar.workout.data

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverters
import com.prafullkumar.workout.data.local.plans.ExercisesTypeConverter
import com.prafullkumar.workout.data.local.plans.WorkoutPlanEntity

data class WorkoutPlan(
    val id: Long = 0L,
    val name: String = "",
    val description: String = "",
    val frequency: Int = 0, // days per week
    val level: ExperienceLevel = ExperienceLevel.BEGINNER,
    val type: PlanType = PlanType.STRENGTH,
    val splits: MutableList<WorkoutSplit> = mutableListOf(),
    val category: PlanCategory = PlanCategory.PREDEFINED,
    val color: Color = Color(0xFF5D4037), // Default color for plans
    val imageUrl: String? = null // Optional background image URL for the plan card
) {
    fun toEntity(): WorkoutPlanEntity {
        return WorkoutPlanEntity(
            id = id,
            name = name,
            description = description,
            frequency = frequency,
            level = level,
            type = type,
            splits = splits.toList(),
            category = category.name,
            colorValue = color.value.toInt(),
            imageUrl = imageUrl
        )
    }
}

data class WorkoutSplit(
    val name: String = "",
    val idx: Long = 0L,
    @TypeConverters(ExercisesTypeConverter::class) val exercises: List<WorkoutExercise> = mutableListOf(),
    val description: String = "",
    val emoji: String = "💪" // Default emoji for the split
)

data class WorkoutExercise(
    val exercise: Int, // ID reference to the exercise
    val weightReps: List<WeightReps>, val restPeriod: Int // in seconds
)

data class WeightReps(
    val weight: Float, val reps: Int
)

enum class PlanCategory {
    PREDEFINED, CUSTOM
}

enum class ExperienceLevel {
    BEGINNER, INTERMEDIATE, ADVANCED;

    fun getColor(): Color {
        return when (this) {
            BEGINNER -> Color(0xFF4CAF50)    // Green
            INTERMEDIATE -> Color(0xFFFFC107) // Amber
            ADVANCED -> Color(0xFFF44336)     // Red
        }
    }
}

enum class PlanType {
    STRENGTH, HYPERTROPHY, ENDURANCE, POWERBUILDING;

    fun getIcon(): String {
        return when (this) {
            STRENGTH -> "🏋️"
            HYPERTROPHY -> "💪"
            ENDURANCE -> "🏃"
            POWERBUILDING -> "⚡"
        }
    }
}

val predefinedWorkoutPlans = listOf(
    WorkoutPlan(
        name = "Push Pull Legs Split",
        description = "A balanced push-pull-legs split designed for hypertrophy.",
        frequency = 6, // days per week
        level = ExperienceLevel.INTERMEDIATE,
        type = PlanType.HYPERTROPHY,
        splits = mutableListOf(
            WorkoutSplit(
                name = "Push Day",
                idx = 1,
                exercises = mutableListOf(
                    WorkoutExercise(
                        exercise = 197,
                        weightReps = listOf(
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8)
                        ),
                        restPeriod = 90
                    ), // Barbell Back Squat
                    WorkoutExercise(
                        exercise = 86,
                        weightReps = listOf(
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8)
                        ),
                        restPeriod = 90
                    ), // Close-Grip Bench Press
                    WorkoutExercise(
                        exercise = 87,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8)
                        ),
                        restPeriod = 90
                    ), // Dips
                    WorkoutExercise(
                        exercise = 88,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8)
                        ),
                        restPeriod = 90
                    ), // Skull Crushers
                    WorkoutExercise(
                        exercise = 204,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 12),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8)
                        ),
                        restPeriod = 60
                    ), // Leg Extensions
                    WorkoutExercise(
                        exercise = 231,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 12),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8)
                        ),
                        restPeriod = 60
                    )  // Seated Calf Raise
                ),
                description = "Focus on pushing movements targeting chest, shoulders, and triceps."
            ), WorkoutSplit(
                name = "Pull Day", idx = 2, exercises = mutableListOf(
                    WorkoutExercise(
                        exercise = 163,
                        weightReps = listOf(
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8)
                        ),
                        restPeriod = 90
                    ), // Deadlifts
                    WorkoutExercise(
                        exercise = 100,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8)
                        ),
                        restPeriod = 90
                    ), // Reverse Barbell Curls
                    WorkoutExercise(
                        exercise = 101,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8)
                        ),
                        restPeriod = 90
                    ), // Hammer Curls
                    WorkoutExercise(
                        exercise = 94,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 12),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8)
                        ),
                        restPeriod = 60
                    ), // Wrist Curls
                    WorkoutExercise(
                        exercise = 95,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 12),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8)
                        ),
                        restPeriod = 60
                    ), // Reverse Wrist Curls
                    WorkoutExercise(
                        exercise = 99,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 12),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8)
                        ),
                        restPeriod = 60
                    )  // Dead Hangs
                ), description = "Focus on pulling movements targeting back, biceps, and forearms."
            ), WorkoutSplit(
                name = "Leg Day", idx = 3, exercises = mutableListOf(
                    WorkoutExercise(
                        exercise = 197,
                        weightReps = listOf(
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8)
                        ),
                        restPeriod = 90
                    ), // Barbell Back Squat
                    WorkoutExercise(
                        exercise = 181,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8)
                        ),
                        restPeriod = 90
                    ), // Romanian Deadlifts
                    WorkoutExercise(
                        exercise = 182,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8)
                        ),
                        restPeriod = 90
                    ), // Bulgarian Split Squats
                    WorkoutExercise(
                        exercise = 183,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 12),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8)
                        ),
                        restPeriod = 60
                    ), // Glute Bridges
                    WorkoutExercise(
                        exercise = 204,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 12),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8)
                        ),
                        restPeriod = 60
                    ), // Leg Extensions
                    WorkoutExercise(
                        exercise = 230,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 12),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8)
                        ),
                        restPeriod = 60
                    )  // Standing Calf Raise
                ), description = "Focus on leg movements targeting quads, hamstrings, and calves."
            )
        )
    ),
    WorkoutPlan(
        name = "Upper Lower Split",
        description = "A balanced upper and lower body workout plan designed for hypertrophy.",
        frequency = 4, // 4 days per week
        level = ExperienceLevel.INTERMEDIATE,
        type = PlanType.HYPERTROPHY,

        splits = mutableListOf(
            WorkoutSplit(
                name = "Upper Body A",
                idx = 1,
                exercises = mutableListOf(
                    WorkoutExercise(
                        exercise = 77,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Dumbbell Bicep Curl
                    WorkoutExercise(
                        exercise = 86,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Close-Grip Bench Press
                    WorkoutExercise(
                        exercise = 83,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // EZ Bar Curl
                    WorkoutExercise(
                        exercise = 87,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Dips
                    WorkoutExercise(
                        exercise = 110,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Hanging Leg Raises
                    WorkoutExercise(
                        exercise = 112,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    )  // Cable Woodchoppers
                ),
                description = "Focuses on upper body muscles including chest, back, shoulders, and arms."
            ), WorkoutSplit(
                name = "Lower Body A",
                idx = 2,
                exercises = mutableListOf(
                    WorkoutExercise(
                        exercise = 197,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Barbell Back Squat
                    WorkoutExercise(
                        exercise = 213,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Romanian Deadlifts
                    WorkoutExercise(
                        exercise = 230,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Standing Calf Raise
                    WorkoutExercise(
                        exercise = 180,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Barbell Hip Thrusts
                    WorkoutExercise(
                        exercise = 189,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Reverse Lunges
                    WorkoutExercise(
                        exercise = 168,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    )  // Superman Hold
                ),
                description = "Focuses on lower body muscles including quads, hamstrings, glutes, and calves."
            ), WorkoutSplit(
                name = "Upper Body B",
                idx = 3,
                exercises = mutableListOf(
                    WorkoutExercise(
                        exercise = 78,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Hammer Curl
                    WorkoutExercise(
                        exercise = 88,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Skull Crushers
                    WorkoutExercise(
                        exercise = 84,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Reverse Curl
                    WorkoutExercise(
                        exercise = 89,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Overhead Dumbbell Triceps Extension
                    WorkoutExercise(
                        exercise = 111,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Toes to Bar
                    WorkoutExercise(
                        exercise = 113,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    )  // Russian Twists
                ),
                description = "Focuses on upper body muscles including chest, back, shoulders, and arms."
            ), WorkoutSplit(
                name = "Lower Body B",
                idx = 4,
                exercises = mutableListOf(
                    WorkoutExercise(
                        exercise = 198,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Front Squat
                    WorkoutExercise(
                        exercise = 214,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Seated Leg Curls
                    WorkoutExercise(
                        exercise = 231,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Seated Calf Raise
                    WorkoutExercise(
                        exercise = 181,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Romanian Deadlifts
                    WorkoutExercise(
                        exercise = 190,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Cable Pull-Throughs
                    WorkoutExercise(
                        exercise = 169,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 3.0f, reps = 8),
                        ),
                        restPeriod = 60
                    )  // Kettlebell Swings
                ),
                description = "Focuses on lower body muscles including quads, hamstrings, glutes, and calves."
            )
        )
    ),
    WorkoutPlan(
        name = "Full Body Split",
        description = "A comprehensive full-body workout plan designed to target all major muscle groups.",
        frequency = 3, // days per week
        level = ExperienceLevel.INTERMEDIATE,
        type = PlanType.HYPERTROPHY,
        splits = mutableListOf(
            WorkoutSplit(
                name = "Day 1: Upper Body Focus",
                idx = 1,
                exercises = mutableListOf(
                    WorkoutExercise(
                        exercise = 77,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Dumbbell Bicep Curl
                    WorkoutExercise(
                        exercise = 86,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Close-Grip Bench Press
                    WorkoutExercise(
                        exercise = 94,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 12),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 45
                    ), // Wrist Curls
                    WorkoutExercise(
                        exercise = 110,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 10),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Hanging Leg Raises
                    WorkoutExercise(
                        exercise = 163,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 90
                    )  // Deadlifts
                ),
                description = "Focus on upper body muscles including arms, chest, forearms, and core."
            ), WorkoutSplit(
                name = "Day 2: Lower Body Focus",
                idx = 2,
                exercises = mutableListOf(
                    WorkoutExercise(
                        exercise = 197,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 90
                    ), // Barbell Back Squat
                    WorkoutExercise(
                        exercise = 180,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 90
                    ), // Barbell Hip Thrusts
                    WorkoutExercise(
                        exercise = 213,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Romanian Deadlifts
                    WorkoutExercise(
                        exercise = 230,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 12),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 45
                    ), // Standing Calf Raise
                    WorkoutExercise(
                        exercise = 110,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 10),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 60
                    )  // Hanging Leg Raises
                ),
                description = "Focus on lower body muscles including quads, glutes, hamstrings, and calves."
            ), WorkoutSplit(
                name = "Day 3: Full Body Focus", idx = 3, exercises = mutableListOf(
                    WorkoutExercise(
                        exercise = 77,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Dumbbell Bicep Curl
                    WorkoutExercise(
                        exercise = 86,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Close-Grip Bench Press
                    WorkoutExercise(
                        exercise = 197,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 90
                    ), // Barbell Back Squat
                    WorkoutExercise(
                        exercise = 180,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 90
                    ), // Barbell Hip Thrusts
                    WorkoutExercise(
                        exercise = 110,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 10),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 60
                    )  // Hanging Leg Raises
                ), description = "A balanced workout targeting both upper and lower body muscles."
            )
        )
    ),
    WorkoutPlan(
        name = "Bro Split",
        description = "A traditional bro split focusing on one major muscle group per day.",
        frequency = 5, // days per week
        level = ExperienceLevel.INTERMEDIATE,
        type = PlanType.HYPERTROPHY,
        splits = mutableListOf(
            WorkoutSplit(
                name = "Chest Day", idx = 1, exercises = mutableListOf(
                    WorkoutExercise(
                        exercise = 1,
                        weightReps = listOf(
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 90
                    ), // Bench Press
                    WorkoutExercise(
                        exercise = 2,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 10),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 60
                    )  // Incline Dumbbell Press
                ), description = "Focus on chest muscles."
            ),
            WorkoutSplit(
                name = "Back Day", idx = 2, exercises = mutableListOf(
                    WorkoutExercise(
                        exercise = 3,
                        weightReps = listOf(
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 90
                    ), // Deadlift
                    WorkoutExercise(
                        exercise = 4,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 10),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 60
                    )  // Bent Over Row
                ), description = "Focus on back muscles."
            ), WorkoutSplit(
                name = "Shoulder Day", idx = 3, exercises = mutableListOf(
                    WorkoutExercise(
                        exercise = 5,
                        weightReps = listOf(
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 90
                    ), // Overhead Press
                    WorkoutExercise(
                        exercise = 6,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 10),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 60
                    )  // Lateral Raise
                ), description = "Focus on shoulder muscles."
            ), WorkoutSplit(
                name = "Leg Day", idx = 4, exercises = mutableListOf(
                    WorkoutExercise(
                        exercise = 7,
                        weightReps = listOf(
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 90
                    ), // Squat
                    WorkoutExercise(
                        exercise = 8,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 10),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 60
                    )  // Leg Press
                ), description = "Focus on leg muscles."
            ), WorkoutSplit(
                name = "Arm Day", idx = 5, exercises = mutableListOf(
                    WorkoutExercise(
                        exercise = 9,
                        weightReps = listOf(
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 90
                    ), // Bicep Curl
                    WorkoutExercise(
                        exercise = 10,
                        weightReps = listOf(
                            WeightReps(weight = 3.0f, reps = 10),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 60
                    )  // Tricep Extension
                ), description = "Focus on arm muscles."
            )
        )
    ),
    WorkoutPlan(
        name = "Arnold Split",
        description = "A classic Arnold Schwarzenegger split focusing on high volume and frequency.",
        frequency = 6, // days per week
        level = ExperienceLevel.ADVANCED,
        type = PlanType.HYPERTROPHY,
        splits = mutableListOf(
            WorkoutSplit(
                name = "Chest and Back", idx = 1, exercises = mutableListOf(
                    WorkoutExercise(
                        exercise = 1,
                        weightReps = listOf(
                            WeightReps(weight = 5.0f, reps = 10),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Bench Press
                    WorkoutExercise(
                        exercise = 2,
                        weightReps = listOf(
                            WeightReps(weight = 5.0f, reps = 10),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 60
                    )  // Bent Over Row
                ), description = "Focus on chest and back muscles."
            ), WorkoutSplit(
                name = "Shoulders and Arms", idx = 2, exercises = mutableListOf(
                    WorkoutExercise(
                        exercise = 3,
                        weightReps = listOf(
                            WeightReps(weight = 5.0f, reps = 10),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Overhead Press
                    WorkoutExercise(
                        exercise = 4,
                        weightReps = listOf(
                            WeightReps(weight = 5.0f, reps = 10),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 60
                    )  // Bicep Curl
                ), description = "Focus on shoulders and arms muscles."
            ), WorkoutSplit(
                name = "Legs", idx = 3, exercises = mutableListOf(
                    WorkoutExercise(
                        exercise = 5,
                        weightReps = listOf(
                            WeightReps(weight = 5.0f, reps = 10),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 60
                    ), // Squat
                    WorkoutExercise(
                        exercise = 6,
                        weightReps = listOf(
                            WeightReps(weight = 5.0f, reps = 10),
                            WeightReps(weight = 4.0f, reps = 8),
                            WeightReps(weight = 4.0f, reps = 8),
                        ),
                        restPeriod = 60
                    )  // Leg Press
                ), description = "Focus on leg muscles."
            )
        )

    ),
)