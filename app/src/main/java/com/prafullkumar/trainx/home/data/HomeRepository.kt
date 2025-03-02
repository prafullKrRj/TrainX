package com.prafullkumar.trainx.home.data

import com.prafullkumar.common.data.local.UserDataDao
import com.prafullkumar.common.data.local.UserDataEntity
import com.prafullkumar.common.domain.enums.ActivityLevel
import com.prafullkumar.common.domain.enums.Gender
import com.prafullkumar.common.domain.enums.Goal
import com.prafullkumar.foodlog.data.endOfDay
import com.prafullkumar.foodlog.data.local.FoodLogDao
import com.prafullkumar.foodlog.data.startOfDay
import com.prafullkumar.trainx.home.data.local.HydrationDao
import com.prafullkumar.trainx.home.data.local.HydrationEntity
import com.prafullkumar.trainx.home.data.local.WaterQuantitySharedPref
import com.prafullkumar.trainx.home.domain.TDEEData
import com.prafullkumar.workout.logging.data.local.UserWorkoutDao
import com.prafullkumar.workout.logging.data.local.UserWorkoutEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.math.RoundingMode

class HomeRepository(
    private val hydrationDao: HydrationDao,
    private val foodLogDao: FoodLogDao,
    private val userWorkoutDao: UserWorkoutDao,
    private val userDataDao: UserDataDao,
    private val waterQuantitySharedPref: WaterQuantitySharedPref
) {

    fun getTotalTodayWaterIntake(): Flow<Double> = hydrationDao.getTotalTodayWaterIntake(
        System.currentTimeMillis()
            .startOfDay(), System.currentTimeMillis().endOfDay()
    )

    fun todayWorkouts(): Flow<List<UserWorkoutEntity>> {
        return userWorkoutDao.getTodayWorkouts(
            System.currentTimeMillis().startOfDay(),
            System.currentTimeMillis().endOfDay()
        )
    }

    suspend fun insertHydration(quantity: Float) {
        hydrationDao.insertHydration(
            HydrationEntity(
                time = System.currentTimeMillis(),
                quantity = quantity
            )
        )
    }

    val totalCaloriesBurned = userWorkoutDao.getTodayCaloriesBurned(
        System.currentTimeMillis().startOfDay(),
        System.currentTimeMillis().endOfDay()
    )

    fun getTotalCalorieConsumedToday() = foodLogDao.getTodayEatenCalories(
        System.currentTimeMillis().startOfDay(),
        System.currentTimeMillis().endOfDay()
    )

    fun getTodayFoodLogs(time: Long) =
        foodLogDao.getTodayFoodLogs(time.startOfDay(), time.endOfDay())

    fun setDailyWaterQuantity(quantity: Float) =
        waterQuantitySharedPref.setDailyWaterQuantity(quantity)

    fun getDailyWaterQuantity() = waterQuantitySharedPref.getDailyWaterQuantity()


    fun getTdeeData(): Flow<TDEEData> = userDataDao.getUserDataFlow().map { userInfoList ->
        val userInfo = userInfoList.first()
        val bmr = calculateBMR(userInfo)
        val tdee = calculateTDEE(userInfo, bmr)
        val proteinIntake = calculateProteinIntake(userInfo, tdee)
        val fatIntake = calculateFatIntake(userInfo, tdee)
        val carbIntake = calculateCarbIntake(tdee, proteinIntake, fatIntake)

        TDEEData(
            tdee = tdee,
            bmr = bmr,
            bmi = calculateBMI(userInfo),
            goal = Goal.valueOf(userInfo.userGoal),
            activityLevel = ActivityLevel.valueOf(userInfo.userActivityLevel),
            protein = proteinIntake,
            fat = fatIntake,
            carbs = carbIntake
        )
    }


    private fun calculateBMR(user: UserDataEntity): Double {
        return if (user.userGender == Gender.MALE.name) {
            10 * user.userWeight + 6.25 * user.userHeight - 5 * user.userAge + 5
        } else {
            10 * user.userWeight + 6.25 * user.userHeight - 5 * user.userAge - 161
        }
    }


    private fun calculateTDEE(user: UserDataEntity, bmr: Double): Double {
        val activityMultiplier = when (ActivityLevel.valueOf(user.userActivityLevel)) {
            ActivityLevel.SEDENTARY -> 1.2
            ActivityLevel.LIGHTLY_ACTIVE -> 1.375
            ActivityLevel.MODERATELY_ACTIVE -> 1.55
            ActivityLevel.VERY_ACTIVE -> 1.725
            ActivityLevel.SUPER_ACTIVE -> 1.9
        }
        return roundToTwoDecimals(bmr * activityMultiplier)
    }


    private fun calculateBMI(user: UserDataEntity): Double {
        val heightM = user.userHeight / 100.0
        return roundToTwoDecimals(user.userWeight / (heightM * heightM))
    }


    private fun calculateIBW(user: UserDataEntity): Double {
        val heightInches = user.userHeight * 0.393701 // Convert cm to inches
        return if (user.userGender == Gender.MALE.name) {
            roundToTwoDecimals(50 + 2.3 * (heightInches - 60))
        } else {
            roundToTwoDecimals(45.5 + 2.3 * (heightInches - 60))
        }
    }

    private fun calculateProteinIntake(user: UserDataEntity, tdee: Double): Double {
        return when (Goal.valueOf(user.userGoal)) {
            Goal.LOSE_WEIGHT -> roundToTwoDecimals(user.userWeight * 2.2)
            Goal.GAIN_WEIGHT -> roundToTwoDecimals(user.userWeight * 1.8)
            Goal.MAINTAIN_WEIGHT -> roundToTwoDecimals(user.userWeight * 1.6)
        }
    }


    private fun calculateFatIntake(user: UserDataEntity, tdee: Double): Double {
        val fatCalories = when (Goal.valueOf(user.userGoal)) {
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


    private fun roundToTwoDecimals(value: Double): Double {
        return BigDecimal(value).setScale(2, RoundingMode.HALF_UP).toDouble()
    }
}