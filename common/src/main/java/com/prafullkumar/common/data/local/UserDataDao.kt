package com.prafullkumar.common.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDataDao {

    @Upsert
    suspend fun insertUserData(userDataEntity: UserDataEntity): Long

    @Query("SELECT * FROM user_data")
    fun getUserDataFlow(): Flow<List<UserDataEntity>>

    @Query("SELECT * FROM user_data")
    suspend fun getUserData(): List<UserDataEntity>


    @Query("UPDATE user_data SET userActivityLevel = :activityLevel where id = 0")
    suspend fun updateActivityLevel(activityLevel: String)

    @Query("UPDATE user_data SET userHeight = :newHeight where id = 0")
    suspend fun updateHeight(newHeight: Double)

    @Query("UPDATE user_data SET userWeight = :newWeight where id = 0")
    suspend fun updateWeight(newWeight: Double)

    @Query("UPDATE user_data SET userGender = :name where id = 0")
    suspend fun updateGender(name: String)

    @Query("UPDATE user_data SET userGoal = :name where id = 0")
    suspend fun updateUserGoal(name: String)
}