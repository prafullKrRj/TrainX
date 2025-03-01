package com.prafullkumar.foodlog.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafullkumar.foodlog.data.local.FoodLogEntity
import com.prafullkumar.foodlog.domain.FoodLogRepository
import com.prafullkumar.foodlog.domain.FoodType
import com.prafullkumar.foodlog.domain.MealType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


data class FoodItem(
    val foodName: String = "",
    val calories: String = "",
    val time: Long = System.currentTimeMillis(),
    val mealType: String,
    val foodType: String = FoodType.VEG.name,
    val protein: String = "",
    val carbs: String = "",
    val fats: String = ""
) {
    fun toFoodEntity() = FoodLogEntity(
        foodName = foodName,
        calories = calories.toInt(),
        time = time,
        mealType = mealType,
        foodType = foodType,
        protein = protein.toIntOrNull() ?: 0,
        carbs = carbs.toIntOrNull() ?: 0,
        fats = fats.toIntOrNull() ?: 0
    )
}

class AddFoodViewModel(
    val mealType: MealType,
    private val foodLogRepository: FoodLogRepository,
    private val context: Context
) : ViewModel() {

    var newFoodItem by mutableStateOf(FoodItem(mealType = mealType.name))

    fun insertFoodLog() {
        if (newFoodItem.foodName.isBlank() || newFoodItem.calories.isEmpty()) {
            Toast.makeText(context, "Please enter necessary data!", Toast.LENGTH_SHORT).show()
            return
        }
        if (newFoodItem.calories.toIntOrNull() == null || newFoodItem.calories.toInt() <= 0) {
            Toast.makeText(context, "Please enter valid calories!", Toast.LENGTH_SHORT).show()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            foodLogRepository.insertFoodLog(
                newFoodItem.copy(time = System.currentTimeMillis()).toFoodEntity()
            )
        }
    }

}