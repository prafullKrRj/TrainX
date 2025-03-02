package com.prafullkumar.trainx.home.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface HydrationDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHydration(hydration: HydrationEntity)

    @Query(
        "SELECT SUM(quantity) FROM HydrationEntity WHERE time BETWEEN :startOfDay AND :endOfDay"
    )
    fun getTotalTodayWaterIntake(startOfDay: Long, endOfDay: Long): Flow<Double>
}