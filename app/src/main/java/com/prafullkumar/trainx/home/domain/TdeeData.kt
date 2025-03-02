package com.prafullkumar.trainx.home.domain

import com.prafullkumar.common.domain.enums.ActivityLevel
import com.prafullkumar.common.domain.enums.Goal

data class TDEEData(
    val tdee: Double = 0.0,
    val bmr: Double = 0.0,
    val bmi: Double = 0.0,
    val goal: Goal = Goal.MAINTAIN_WEIGHT,
    val activityLevel: ActivityLevel = ActivityLevel.SEDENTARY,
    val protein: Double = 0.0,
    val fat: Double = 0.0,
    val carbs: Double = 0.0
)