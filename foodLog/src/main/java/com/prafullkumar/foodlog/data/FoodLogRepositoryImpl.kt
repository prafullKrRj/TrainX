package com.prafullkumar.foodlog.data

import android.util.Log
import com.prafullkumar.common.data.local.UserDataDao
import com.prafullkumar.common.data.local.UserDataEntity
import com.prafullkumar.common.domain.enums.ActivityLevel
import com.prafullkumar.common.domain.enums.Gender
import com.prafullkumar.common.domain.enums.Goal
import com.prafullkumar.foodlog.data.local.FoodLogDao
import com.prafullkumar.foodlog.data.local.FoodLogEntity
import com.prafullkumar.foodlog.domain.FoodLogRepository
import com.prafullkumar.foodlog.domain.TDEEData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Calendar

class FoodLogRepositoryImpl(
    private val foodLogDao: FoodLogDao
) : FoodLogRepository, KoinComponent {

    private val dao: UserDataDao by inject()

    override suspend fun insertFoodLog(food: FoodLogEntity): Long {
        return foodLogDao.insertFoodLog(food)
    }

    override fun getAllFoods(): Flow<List<FoodLogEntity>> = foodLogDao.getAllFoodLogs()


    override fun getTodayFoods(): Flow<List<FoodLogEntity>> {
        return foodLogDao.getTodayFoodLogs(
            start = System.currentTimeMillis().startOfDay(),
            end = System.currentTimeMillis().endOfDay()
        )
    }

    override fun getTodayCalories(): Flow<Int> {
        return foodLogDao.getTodayEatenCalories(
            start = System.currentTimeMillis().startOfDay(),
            end = System.currentTimeMillis().endOfDay()
        )
    }

    override fun deleteFoodLog(food: FoodLogEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFoodLogs(list: List<Int>) {
        Log.d("FoodLogRepositoryImpl", "deleteFoodLogs: $list")
        foodLogDao.deleteFoodLogs(list)
    }

    override fun getTdeeData(): Flow<TDEEData> {
        return dao.getUserDataFlow().map { user ->
            Log.d("FoodLogRepositoryImpl", "getTdeeData: $user")
            val bmr = calculateBMR(user[0])
            val tdee = calculateTDEE(user[0], bmr, Goal.valueOf(user[0].userGoal))
            val goal = Goal.valueOf(user[0].userGoal)
            TDEEData(
                tdee = roundToTwoDecimals(tdee),
                goal = goal,
                protein = calculateProteinIntake(user[0], goal),
                fat = calculateFatIntake(user[0], tdee, goal),
                carbs = calculateCarbIntake(tdee, calculateProteinIntake(user[0], goal), calculateFatIntake(user[0], tdee, goal))
            )
        }
    }

    private fun calculateTDEE(userDataEntity: UserDataEntity, bmr: Double, goal: Goal): Double {
        val activityLevel = ActivityLevel.valueOf(userDataEntity.userActivityLevel)
        return when (activityLevel) {
            ActivityLevel.SEDENTARY -> {
                when (goal) {
                    Goal.LOSE_WEIGHT -> bmr * 1.2
                    Goal.GAIN_WEIGHT -> bmr * 1.4
                    Goal.MAINTAIN_WEIGHT -> bmr * 1.3
                }
            }
            ActivityLevel.LIGHTLY_ACTIVE -> {
                when (goal) {
                    Goal.LOSE_WEIGHT -> bmr * 1.375
                    Goal.GAIN_WEIGHT -> bmr * 1.575
                    Goal.MAINTAIN_WEIGHT -> bmr * 1.475
                }
            }
            ActivityLevel.MODERATELY_ACTIVE -> {
                when (goal) {
                    Goal.LOSE_WEIGHT -> bmr * 1.55
                    Goal.GAIN_WEIGHT -> bmr * 1.75
                    Goal.MAINTAIN_WEIGHT -> bmr * 1.65
                }
            }
            ActivityLevel.VERY_ACTIVE -> {
                when (goal) {
                    Goal.LOSE_WEIGHT -> bmr * 1.725
                    Goal.GAIN_WEIGHT -> bmr * 1.925
                    Goal.MAINTAIN_WEIGHT -> bmr * 1.825
                }
            }
            ActivityLevel.SUPER_ACTIVE -> {
                when (goal) {
                    Goal.LOSE_WEIGHT -> bmr * 1.9
                    Goal.GAIN_WEIGHT -> bmr * 2.1
                    Goal.MAINTAIN_WEIGHT -> bmr * 2.0
                }
            }
        }

    }

    private fun calculateProteinIntake(user: UserDataEntity, goal: Goal): Double {
        return when (goal) {
            Goal.LOSE_WEIGHT -> roundToTwoDecimals(user.userWeight * 2.2) // Higher for muscle retention
            Goal.GAIN_WEIGHT -> roundToTwoDecimals(user.userWeight * 1.8)
            Goal.MAINTAIN_WEIGHT -> roundToTwoDecimals(user.userWeight * 1.6)
        }
    }

    private fun calculateFatIntake(user: UserDataEntity, tdee: Double, goal: Goal): Double {
        val fatCalories = when (goal) {
            Goal.LOSE_WEIGHT -> tdee * 0.20 // Lower fat intake for weight loss
            Goal.GAIN_WEIGHT -> tdee * 0.35
            Goal.MAINTAIN_WEIGHT -> tdee * 0.25
        }
        return roundToTwoDecimals(fatCalories / 9) // 1g of fat = 9 kcal
    }

    private fun calculateCarbIntake(tdee: Double, protein: Double, fat: Double): Double {
        val proteinCalories = protein * 4 // 1g protein = 4 kcal
        val fatCalories = fat * 9
        val remainingCalories = tdee - (proteinCalories + fatCalories)
        return roundToTwoDecimals(remainingCalories / 4) // 1g carb = 4 kcal
    }

    private fun calculateBMR(user: UserDataEntity): Double {
        return if (user.userGender == Gender.MALE.name) {
            10 * user.userWeight + 6.25 * user.userHeight - 5 * user.userAge + 5
        } else {
            10 * user.userWeight + 6.25 * user.userHeight - 5 * user.userAge - 161
        }
    }

    private fun roundToTwoDecimals(value: Double): Double {
        return BigDecimal(value).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

}

fun Long.startOfDay(): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

fun Long.endOfDay(): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    return calendar.timeInMillis
}