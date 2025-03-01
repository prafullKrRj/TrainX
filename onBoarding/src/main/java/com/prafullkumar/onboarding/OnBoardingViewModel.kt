package com.prafullkumar.onboarding

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafullkumar.common.domain.enums.ActivityLevel
import com.prafullkumar.common.domain.enums.Gender
import com.prafullkumar.common.domain.enums.Goal
import com.prafullkumar.common.domain.model.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class OnBoardingUIState(
    val gender: Gender = Gender.MALE,
    val name: String = "",
    val age: String = "",
    val weight: String = "",
    val height: String = "",
    val goal: Goal = Goal.MAINTAIN_WEIGHT,
    val activityLevel: ActivityLevel = ActivityLevel.SEDENTARY
) {
    fun toUserData() = UserData(
        userName = name,
        userGender = gender.name,
        userWeight = weight.toInt(),
        userHeight = height.toInt(),
        userGoal = goal.name,
        userActivityLevel = activityLevel.name,
        userAge = age.toInt(),
    )
}

class OnBoardingViewModel : ViewModel(), KoinComponent {
    private val onBoardingRepository: OnBoardingRepository by inject()
    private val context: Context by inject()
    var uiState by mutableStateOf(OnBoardingUIState())


    fun canMoveToActivitySelectionScreen(): Boolean {
        if (uiState.weight.toIntOrNull() == null) {
            showToast("Enter valid weight!")
            return false
        }
        if (uiState.height.toIntOrNull() == null) {
            showToast("Enter valid height!")
            return false
        }
        if (uiState.age.toIntOrNull() == null) {
            showToast("Enter valid age!")
            return false
        }
        if (uiState.height.isEmpty() || uiState.weight.isEmpty() || uiState.age.isEmpty() || uiState.name.isEmpty()) {
            showToast("Enter correct values!")
            return false;
        }
        return true;
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun saveUserDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            onBoardingRepository.insertData(uiState.toUserData())
        }
    }
}