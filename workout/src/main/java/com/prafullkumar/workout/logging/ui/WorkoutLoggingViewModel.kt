package com.prafullkumar.workout.logging.ui


import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafullkumar.workout.data.WorkoutPlan
import com.prafullkumar.workout.data.WorkoutSplit
import com.prafullkumar.workout.data.exercise.exercises
import com.prafullkumar.workout.logging.data.WorkoutLoggingRepository
import com.prafullkumar.workout.logging.data.local.CompletionStatus
import com.prafullkumar.workout.logging.data.local.LoggedExercise
import com.prafullkumar.workout.logging.data.local.LoggedExerciseSet
import com.prafullkumar.workout.logging.data.local.UserWorkoutEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class WorkoutLoggingViewModel(
    private val repository: WorkoutLoggingRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutLoggingUiState())
    val uiState: StateFlow<WorkoutLoggingUiState> = _uiState.asStateFlow()

    private val _workoutPlan = MutableStateFlow<WorkoutPlan?>(null)
    private val workoutSplit = MutableStateFlow<WorkoutSplit?>(null)
    val workoutPlan: StateFlow<WorkoutPlan?> = _workoutPlan.asStateFlow()

    init {
        viewModelScope.launch {
            val planId = savedStateHandle.get<Long>("planId")
            val splitIndex = savedStateHandle.get<Long>("splitIndex")
            Log.d("WorkoutLoggingViewModel", "planId: $planId, splitIndex: $splitIndex")
            _workoutPlan.update {
                repository.getWorkoutPlan(planId ?: 0L).toWorkoutPlan()
            }
            workoutSplit.update {
                splitIndex?.toInt()?.let { it1 -> workoutPlan.value?.splits?.get(it1) }
            }
            Log.d("WorkoutLoggingViewModel", "WorkoutPlan: ${_workoutPlan.value}")
            if (workoutSplit.value != null) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        planName = _workoutPlan.value?.name ?: "",
                        splitName = workoutSplit.value?.name ?: "",
                        exercisesToLog = workoutSplit.value!!.exercises.map { exercise ->
                            val sets = mutableListOf<ExerciseSetUiState>()
                            exercise.weightReps.forEachIndexed { index, weightRep ->
                                sets.add(
                                    ExerciseSetUiState(
                                        setNumber = sets.size + 1,
                                        weight = weightRep.weight.toFloat(),
                                        reps = weightRep.reps,
                                        isCompleted = false
                                    )
                                )
                            }
                            ExerciseUiState(
                                exerciseId = exercise.exercise.toLong(),
                                exerciseName = getExerciseName(exercise.exercise.toLong()),
                                targetSets = exercise.weightReps.size,
                                targetReps = exercise.weightReps.firstOrNull()?.reps ?: 0,
                                restPeriod = exercise.restPeriod,
                                sets = sets
                            )
                        }
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        errorMessage = "Could not find the specified workout plan or split",
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun getExerciseName(exerciseId: Long): String {
        return exercises[exerciseId.toInt()].name
    }

    fun updateSet(
        exerciseIndex: Int,
        setIndex: Int,
        weight: Float?,
        reps: Int,
        isCompleted: Boolean
    ) {
        val exercises = _uiState.value.exercisesToLog.toMutableList()
        val exercise = exercises[exerciseIndex]
        val sets = exercise.sets.toMutableList()
        sets[setIndex] = ExerciseSetUiState(
            setNumber = setIndex + 1,
            weight = weight,
            reps = reps,
            isCompleted = isCompleted
        )
        exercises[exerciseIndex] = exercise.copy(sets = sets)
        _uiState.update { it.copy(exercisesToLog = exercises) }
    }

    fun updateExerciseNotes(exerciseIndex: Int, notes: String) {
        val exercises = _uiState.value.exercisesToLog.toMutableList()
        exercises[exerciseIndex] = exercises[exerciseIndex].copy(notes = notes)
        _uiState.update { it.copy(exercisesToLog = exercises) }
    }

    fun updateWorkoutNotes(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun updateFeeling(feelingBefore: String?, feelingAfter: String?) {
        _uiState.update { it.copy(feelingBefore = feelingBefore, feelingAfter = feelingAfter) }
    }

    fun updateRating(rating: Int) {
        _uiState.update { it.copy(rating = rating) }
    }

    fun saveWorkout(caloriesBurned: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, calorieBurn = caloriesBurned) }

            try {
                val exercises = _uiState.value.exercisesToLog.map { exerciseUi ->
                    LoggedExercise(
                        exerciseId = exerciseUi.exerciseId,
                        exerciseName = exerciseUi.exerciseName,
                        sets = exerciseUi.sets.map { setUi ->
                            LoggedExerciseSet(
                                setNumber = setUi.setNumber,
                                weight = setUi.weight,
                                reps = setUi.reps,
                                isCompleted = setUi.isCompleted,
                                notes = null
                            )
                        },
                        notes = exerciseUi.notes,
                        completionStatus = if (exerciseUi.sets.all { it.isCompleted })
                            CompletionStatus.COMPLETED else CompletionStatus.PARTIAL
                    )
                }

                val userWorkout = UserWorkoutEntity(
                    planId = _workoutPlan.value?.id ?: 0,
                    splitName = _uiState.value.splitName,
                    date = Date(),
                    duration = _uiState.value.duration,
                    exercises = exercises,
                    completionStatus = determineCompletionStatus(exercises),
                    notes = _uiState.value.notes,
                    rating = _uiState.value.rating,
                    caloriesBurned = caloriesBurned,
                    feelingBefore = _uiState.value.feelingBefore,
                    feelingAfter = _uiState.value.feelingAfter
                )

                repository.saveUserWorkout(userWorkout, workoutPlan.value!!, workoutSplit.value!!)
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = "Failed to save workout: ${e.message}"
                    )
                }
            }
        }
    }

    private fun determineCompletionStatus(exercises: List<LoggedExercise>): CompletionStatus {
        val completedExercises =
            exercises.count { it.completionStatus == CompletionStatus.COMPLETED }
        return when {
            completedExercises == 0 -> CompletionStatus.SKIPPED
            completedExercises == exercises.size -> CompletionStatus.COMPLETED
            else -> CompletionStatus.PARTIAL
        }
    }

    fun startWorkout() {
        _uiState.update { it.copy(isStarted = true, startTime = System.currentTimeMillis()) }
    }

    fun finishWorkout() {
        val endTime = System.currentTimeMillis()
        val startTime = _uiState.value.startTime
        val durationMinutes = ((endTime - startTime) / 60000).toInt()
        _uiState.update { it.copy(duration = durationMinutes) }
    }

    fun addSet(exerciseIndex: Int) {
        val exercises = _uiState.value.exercisesToLog.toMutableList()
        val exercise = exercises[exerciseIndex]
        val sets = exercise.sets.toMutableList()

        val lastSet = sets.lastOrNull()
        val newSet = lastSet?.copy(
            setNumber = sets.size + 1,
            isCompleted = false
        ) ?: ExerciseSetUiState(
            setNumber = 1,
            weight = null,
            reps = 0,
            isCompleted = false
        )

        sets.add(newSet)
        exercises[exerciseIndex] = exercise.copy(sets = sets)
        _uiState.update { it.copy(exercisesToLog = exercises) }
    }
}

data class WorkoutLoggingUiState(
    val isLoading: Boolean = true,
    val isStarted: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val planName: String = "",
    val splitName: String = "",
    val exercisesToLog: List<ExerciseUiState> = emptyList(),
    val startTime: Long = 0,
    val duration: Int = 0,
    val notes: String? = null,
    val rating: Int? = null,
    val calorieBurn: Int = 0,
    val feelingBefore: String? = null,
    val feelingAfter: String? = null,
    val errorMessage: String? = null
)

data class ExerciseUiState(
    val exerciseId: Long,
    val exerciseName: String,
    val targetSets: Int,
    val targetReps: Int,
    val restPeriod: Int,
    val sets: List<ExerciseSetUiState> = emptyList(),
    val notes: String? = null
)

data class ExerciseSetUiState(
    val setNumber: Int,
    val weight: Float?,
    val reps: Int,
    val isCompleted: Boolean
)