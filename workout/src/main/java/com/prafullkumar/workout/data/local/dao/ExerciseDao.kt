package com.prafullkumar.workout.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.prafullkumar.workout.data.local.ExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Insert(entity = ExerciseEntity::class)
    suspend fun insertExercises(exercises: List<ExerciseEntity>)

    @Query("SELECT * FROM exercises")
    fun getAllExercises(): Flow<List<ExerciseEntity>>

    @Query("SELECT COUNT(*) FROM exercises")
    fun getExerciseCount(): Long
}