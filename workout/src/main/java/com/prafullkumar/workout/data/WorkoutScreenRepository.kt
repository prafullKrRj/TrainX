package com.prafullkumar.workout.data

import com.prafullkumar.workout.data.local.EquipmentEntity
import com.prafullkumar.workout.data.local.dao.EquipmentDao
import com.prafullkumar.workout.data.local.dao.ExerciseDao
import com.prafullkumar.workout.data.local.plans.WorkoutPlansDao
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WorkoutScreenRepository : KoinComponent {
    private val equipmentDao: EquipmentDao by inject()
    private val workoutPlansDao by inject<WorkoutPlansDao>()
    fun getExerciseEquipment(equipmentId: Int): Flow<EquipmentEntity> {
        return equipmentDao.getEquipmentById(equipmentId)
    }

    suspend fun getPredefinedPlans() = workoutPlansDao.getPredefinedPlans()

    suspend fun savePlan(newPlan: WorkoutPlan) {
        workoutPlansDao.insertPlan(newPlan.toEntity())
    }

    fun getCustomPlans() = workoutPlansDao.getCustomPlans()

    private val exerciseDao: ExerciseDao by inject()

    val exercises = exerciseDao.getAllExercises()
    val equipments = equipmentDao.getAllEquipmentFlow()

    suspend fun getPlanDetails(planId: Long) = workoutPlansDao.getPlanDetails(planId)
}