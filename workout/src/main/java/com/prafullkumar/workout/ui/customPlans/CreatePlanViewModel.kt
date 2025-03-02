package com.prafullkumar.workout.ui.customPlans

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafullkumar.workout.data.PlanCategory
import com.prafullkumar.workout.data.WorkoutPlan
import com.prafullkumar.workout.data.WorkoutScreenRepository
import kotlinx.coroutines.launch

class CreatePlanViewModel(
    private val repository: WorkoutScreenRepository,
    private val context: Context
) : ViewModel() {
    var newPlan by mutableStateOf(
        WorkoutPlan(
            name = "New Training Plan",
            category = PlanCategory.CUSTOM
        )
    )

    private fun validatePlan(): Boolean {
        if (newPlan.name.isBlank()) {
            showToast("Plan name cannot be empty")
            return false
        }
        if (newPlan.splits.isEmpty()) {
            showToast("Add at least one split to the plan")
            return false
        }
        if (newPlan.frequency <= 0) {
            showToast("Training frequency must be at least 1 day per week")
            return false
        }
        for (split in newPlan.splits) {
            if (split.exercises.isEmpty()) {
                showToast("Each split must have at least one exercise")
                return false
            }
        }
        return true
    }

    fun savePlan() {
        if (!validatePlan()) return
        viewModelScope.launch {
            try {
                repository.savePlan(newPlan)
                showToast("Plan saved successfully")
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("Failed to save plan")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


}