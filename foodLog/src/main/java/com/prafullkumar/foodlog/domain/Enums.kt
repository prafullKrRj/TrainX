package com.prafullkumar.foodlog.domain

import androidx.annotation.DrawableRes
import com.prafullkumar.foodlog.R

enum class MealType(
    @DrawableRes val icon: Int
) {
    BREAKFAST(R.drawable.breakfast), LUNCH(R.drawable.lunch), DINNER(R.drawable.dinner), SNACK(R.drawable.snacks)
}

enum class FoodType {
    VEG, NON_VEG
}

enum class Macros {
    PROTEIN, CARBS, FATS
}