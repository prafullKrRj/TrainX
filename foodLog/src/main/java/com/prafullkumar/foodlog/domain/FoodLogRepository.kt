package com.prafullkumar.foodlog.domain

import com.prafullkumar.foodlog.data.local.FoodLogEntity
import kotlinx.coroutines.flow.Flow

interface FoodLogRepository {

    suspend fun insertFoodLog(food: FoodLogEntity): Long
    fun getAllFoods(): Flow<List<FoodLogEntity>>
    fun getTodayFoods(): Flow<List<FoodLogEntity>>

    fun getTodayCalories(): Flow<Int>

    fun deleteFoodLog(food: FoodLogEntity)

    suspend fun deleteFoodLogs(list: List<Int>)

    fun getTdeeData(): Flow<TDEEData>
}