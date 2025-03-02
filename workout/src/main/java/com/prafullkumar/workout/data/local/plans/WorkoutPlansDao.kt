package com.prafullkumar.workout.data.local.plans

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.prafullkumar.workout.data.PlanCategory
import kotlinx.coroutines.flow.Flow


@Dao
interface WorkoutPlansDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: WorkoutPlanEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlans(plans: List<WorkoutPlanEntity>)

    @Query("SELECT * FROM workout_plans WHERE category = :category")
    suspend fun getPredefinedPlans(
        category: String = PlanCategory.PREDEFINED
            .name
    ): List<WorkoutPlanEntity>

    @Query("SELECT * FROM workout_plans WHERE category = :category")
    fun getCustomPlans(
        category: String = PlanCategory.CUSTOM
            .name
    ): Flow<List<WorkoutPlanEntity>>

    @Query("SELECT * FROM workout_plans WHERE id = :planId")
    suspend fun getPlanDetails(planId: Long): WorkoutPlanEntity

    @Update
    suspend fun updatePlan(plan: WorkoutPlanEntity)
}