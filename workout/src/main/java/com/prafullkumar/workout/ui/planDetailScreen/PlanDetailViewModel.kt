package com.prafullkumar.workout.ui.planDetailScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafullkumar.workout.data.WorkoutPlan
import com.prafullkumar.workout.data.WorkoutScreenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlanDetailViewModel(
    private val repository: WorkoutScreenRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val _state = MutableStateFlow<WorkoutPlan?>(null)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                Log.d(
                    "PlanDetailViewModel",
                    "PlanDetailViewModel: ${savedStateHandle.get<Long>("id")}"
                )
                _state.update {
                    repository.getPlanDetails(savedStateHandle.get<Long>("id") ?: 0L)
                        .toWorkoutPlan()
                }
            } catch (e: Exception) {
                _state.value = null
            }
        }
    }
}