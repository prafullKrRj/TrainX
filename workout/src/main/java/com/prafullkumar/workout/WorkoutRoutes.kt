package com.prafullkumar.workout

import kotlinx.serialization.Serializable

sealed interface WorkoutRoutes {

    @Serializable
    data object Main : WorkoutRoutes

    @Serializable
    data class WorkoutPlanDetails(val id: Long) : WorkoutRoutes

    @Serializable
    data object CreateWorkoutPlan : WorkoutRoutes


    @Serializable
    data class LogWorkout(val planId: Long, val splitIndex: Long) : WorkoutRoutes

    @Serializable
    data object WorkingOutPlan : WorkoutRoutes
}