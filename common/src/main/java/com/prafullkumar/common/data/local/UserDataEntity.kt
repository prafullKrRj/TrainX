package com.prafullkumar.common.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prafullkumar.common.domain.model.UserData

@Entity(tableName = "user_data")
data class UserDataEntity(
    @PrimaryKey
    val id: Int = 0,
    val userName: String,
    val userWeight: Int,
    val userHeight: Int,
    val userGoal: String,
    val userGender: String,
    val userAge: Int,
    val userActivityLevel: String
) {
    fun toUserData() = UserData(
        userName = userName,
        userWeight = userWeight,
        userHeight = userHeight,
        userGoal = userGoal,
        userGender = userGender,
        userAge = userAge,
        userActivityLevel = userActivityLevel
    )
}