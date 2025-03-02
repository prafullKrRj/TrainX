package com.prafullkumar.trainx.home.data.local

import android.content.Context

class WaterQuantitySharedPref(
    private val context: Context
) {
    companion object {
        private const val DAILY_WATER_QUANTITY = "daily_water_quantity"
    }

    fun getDailyWaterQuantity(): Float {
        val sharedPref = context.getSharedPreferences(DAILY_WATER_QUANTITY, Context.MODE_PRIVATE)
        return sharedPref.getFloat(DAILY_WATER_QUANTITY, 2.5f)
    }

    fun setDailyWaterQuantity(quantity: Float) {
        val sharedPref = context.getSharedPreferences(DAILY_WATER_QUANTITY, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putFloat(DAILY_WATER_QUANTITY, quantity)
            apply()
        }
    }
}