package com.prafullkumar.trainx.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafullkumar.trainx.home.data.HomeRepository
import com.prafullkumar.trainx.home.domain.TDEEData
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class HomeViewModel(
    private val repository: HomeRepository
) : ViewModel(), KoinComponent {

    val todayCaloriesBurned = repository.totalCaloriesBurned.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0.0
    )

    var dailyWaterIntake by mutableFloatStateOf(repository.getDailyWaterQuantity())

    val tdeeData =
        repository.getTdeeData()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TDEEData())

    fun updateDailyWaterIntake(quantity: Float) {
        viewModelScope.launch {
            dailyWaterIntake = quantity
            repository.setDailyWaterQuantity(quantity)
        }
    }

    val todayWorkouts = repository.todayWorkouts().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val totalWaterIntake = repository.getTotalTodayWaterIntake().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0.0
    )

    fun insertHydration(quantity: Float) {
        viewModelScope.launch {
            repository.insertHydration(quantity)
        }
    }

    val todayConsumedCalories = repository.getTotalCalorieConsumedToday().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), 0
    )
    val todayFoodLogs = repository.getTodayFoodLogs(System.currentTimeMillis()).stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )
}