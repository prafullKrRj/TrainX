package com.prafullkumar.workout.data

import com.prafullkumar.workout.data.exercise.exercises
import com.prafullkumar.workout.data.local.dao.EquipmentDao
import com.prafullkumar.workout.data.local.dao.ExerciseDao
import com.prafullkumar.workout.data.local.plans.WorkoutPlansDao
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PopulatingRepository : KoinComponent {
    private val exerciseDao: ExerciseDao by inject()
    private val equipmentDao: EquipmentDao by inject()
    private val workoutPlansDao by inject<WorkoutPlansDao>()
    suspend fun populateDatabase() {

        if (equipmentDao.getAllEquipment().isNotEmpty()) {
            return
        }
        coroutineScope {
            if (workoutPlansDao.getPredefinedPlans().isNotEmpty()) {
                return@coroutineScope
            }
            workoutPlansDao.insertPlans(predefinedWorkoutPlans.map { it.toEntity() })
        }
        equipmentDao.insertAll(gymEquipment.map {
            it.toEquipmentEntity()
        })
        exerciseDao.insertExercises(exercises.map {
            it.toExerciseEntity()
        })

    }
}