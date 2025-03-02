package com.prafullkumar.workout.logging.data

import com.prafullkumar.workout.data.WeightReps
import com.prafullkumar.workout.data.WorkoutExercise
import com.prafullkumar.workout.data.WorkoutPlan
import com.prafullkumar.workout.data.WorkoutSplit
import com.prafullkumar.workout.data.local.plans.WorkoutPlansDao
import com.prafullkumar.workout.logging.data.local.UserWorkoutDao
import com.prafullkumar.workout.logging.data.local.UserWorkoutEntity
import kotlinx.coroutines.coroutineScope

class WorkoutLoggingRepository(
    private val workoutLoggingDao: UserWorkoutDao,
    private val workoutPlansDao: WorkoutPlansDao
) {
    val userWorkouts = workoutLoggingDao.getUserWorkouts()

    suspend fun saveUserWorkout(
        userWorkout: UserWorkoutEntity,
        plan: WorkoutPlan,
        split: WorkoutSplit
    ) {
        coroutineScope {
            workoutLoggingDao.insertUserWorkout(userWorkout)
        }
        coroutineScope {
            var splits = plan.splits
            splits[split.idx.toInt()] = splits[split.idx.toInt()].copy(
                exercises = userWorkout.exercises.map { exercise ->
                    WorkoutExercise(
                        exercise = exercise.exerciseId.toInt(),
                        weightReps = exercise.sets.map { set ->
                            WeightReps(
                                weight = set.weight ?: 0.0f,
                                reps = set.reps
                            )
                        },
                        restPeriod = split.exercises.find { it.exercise == exercise.exerciseId.toInt() }?.restPeriod
                            ?: 0
                    )
                }
            )
            workoutPlansDao.updatePlan(plan.copy(splits = splits).toEntity())
        }
    }

    suspend fun getWorkoutPlan(planId: Long) = workoutPlansDao.getPlanDetails(planId)
}