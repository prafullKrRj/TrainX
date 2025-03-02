package com.prafullkumar.workout.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafullkumar.workout.data.WorkoutPlan
import com.prafullkumar.workout.data.WorkoutScreenRepository
import com.prafullkumar.workout.data.local.EquipmentEntity
import com.prafullkumar.workout.data.local.ExerciseEntity
import com.prafullkumar.workout.logging.data.WorkoutLoggingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WorkoutViewModel(
    private val repository: WorkoutScreenRepository
) : ViewModel(), KoinComponent {

    private val userLoggingRepository by inject<WorkoutLoggingRepository>()
    private val _preDefinedPlans = MutableStateFlow<List<WorkoutPlan>>(emptyList())
    val preDefinedPlans = _preDefinedPlans.asStateFlow()

    val history = userLoggingRepository.userWorkouts.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val customPlans =
        repository.getCustomPlans().map { entityList -> entityList.map { it.toWorkoutPlan() } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            _preDefinedPlans.update {
                repository.getPredefinedPlans().map { it.toWorkoutPlan() }
            }
        }
    }

    val exercises: StateFlow<List<ExerciseEntity>> = repository.exercises.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )
    val equipments: StateFlow<List<EquipmentEntity>> = repository.equipments.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun getExerciseEquipment(equipmentId: Int): Flow<EquipmentEntity> =
        repository.getExerciseEquipment(equipmentId)
}