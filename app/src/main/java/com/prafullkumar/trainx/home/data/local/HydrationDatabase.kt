package com.prafullkumar.trainx.home.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [HydrationEntity::class], version = 1, exportSchema = false)
abstract class HydrationDatabase : RoomDatabase() {

    abstract fun hydrationDao(): HydrationDao
}