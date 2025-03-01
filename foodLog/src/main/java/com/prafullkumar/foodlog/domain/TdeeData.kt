package com.prafullkumar.foodlog.domain

import com.prafullkumar.common.domain.enums.ActivityLevel
import com.prafullkumar.common.domain.enums.Goal

data class TDEEData(
    val tdee: Double,
    val goal: Goal,
    val protein: Double,
    val fat: Double,
    val carbs: Double
)