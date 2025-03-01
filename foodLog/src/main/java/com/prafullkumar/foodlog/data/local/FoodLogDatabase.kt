package com.prafullkumar.foodlog.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [FoodLogEntity::class], version = 1, exportSchema = false)
abstract class FoodLogDatabase : RoomDatabase() {
    abstract fun foodLogDao(): FoodLogDao
}