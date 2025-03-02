package com.prafullkumar.workout.logging.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserWorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserWorkout(userWorkout: UserWorkoutEntity): Long

    @Query("SELECT * FROM user_workouts")
    fun getUserWorkouts(): Flow<List<UserWorkoutEntity>>


    @Query("SELECT * FROM user_workouts WHERE date BETWEEN :startOfDay AND :endOfDay")
    fun getTodayWorkouts(startOfDay: Long, endOfDay: Long): Flow<List<UserWorkoutEntity>>

    @Query("SELECT SUM(caloriesBurned) FROM user_workouts WHERE date BETWEEN :startOfDay AND :endOfDay")
    abstract fun getTodayCaloriesBurned(startOfDay: Long, endOfDay: Long): Flow<Double>
}