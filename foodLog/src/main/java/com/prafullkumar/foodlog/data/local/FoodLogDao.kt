package com.prafullkumar.foodlog.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FoodLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodLog(foodLogEntity: FoodLogEntity): Long

    @Query("SELECT * FROM food_log order by time desc")
    fun getAllFoodLogs(): Flow<List<FoodLogEntity>>

    @Query("SELECT SUM(calories) FROM food_log where time BETWEEN :start AND :end")
    fun getTodayEatenCalories(start: Long, end: Long): Flow<Int>


    @Query("DELETE FROM food_log WHERE id IN (:toList)")
    suspend fun deleteFoodLogs(toList: List<Int>)

    @Query("SELECT * FROM food_log WHERE time BETWEEN :start AND :end")
    fun getTodayFoodLogs(start: Long, end: Long): Flow<List<FoodLogEntity>>
}