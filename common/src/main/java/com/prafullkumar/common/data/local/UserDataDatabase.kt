package com.prafullkumar.common.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserDataEntity::class], version = 1, exportSchema = false)
abstract class UserDataDatabase : RoomDatabase() {
    /**
     * Abstract method to get the DAO for UserDataEntity.
     *
     * @return UserDataEntityDao The DAO for UserDataEntity.
     */
    abstract fun userDataEntityDao(): UserDataDao
}