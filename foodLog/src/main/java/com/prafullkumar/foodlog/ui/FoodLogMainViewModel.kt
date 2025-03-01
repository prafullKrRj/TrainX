package com.prafullkumar.foodlog.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafullkumar.common.domain.enums.Goal
import com.prafullkumar.foodlog.domain.FoodLogRepository
import com.prafullkumar.foodlog.domain.TDEEData
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class FoodLogMainViewModel(
    private val foodLogRepository: FoodLogRepository
) : ViewModel() {

    val currentEatenCalories = foodLogRepository.getTodayCalories()
        .stateIn(viewModelScope, started = SharingStarted.Lazily, 0)

    val tdeeData = foodLogRepository.getTdeeData()
        .stateIn(viewModelScope, started = SharingStarted.Lazily, TDEEData(0.0, Goal.MAINTAIN_WEIGHT, 0.0, 0.0, 0.0))

    val todayFoods = foodLogRepository.getTodayFoods()
        .stateIn(viewModelScope, started = SharingStarted.Lazily, emptyList())
}