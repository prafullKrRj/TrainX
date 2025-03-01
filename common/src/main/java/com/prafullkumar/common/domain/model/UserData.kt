package com.prafullkumar.common.domain.model

import com.prafullkumar.common.data.local.UserDataEntity

data class UserData(
    val id: Int = 0,
    val userName: String,
    val userWeight: Int,
    val userHeight: Int,
    val userGoal: String,
    val userGender: String,
    val userAge: Int,
    val userActivityLevel: String
) {
    fun toUserDataEntity() = UserDataEntity(
        id = 0,
        userName = userName,
        userWeight = userWeight,
        userHeight = userHeight,
        userGoal = userGoal,
        userGender = userGender,
        userAge = userAge,
        userActivityLevel = userActivityLevel
    )
}