package com.prafullkumar.workout.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prafullkumar.workout.data.local.EquipmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EquipmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(equipment: List<EquipmentEntity>)

    @Query("SELECT * FROM equipment")
    suspend fun getAllEquipment(): List<EquipmentEntity>

    @Query("SELECT * FROM equipment")
    fun getAllEquipmentFlow(): Flow<List<EquipmentEntity>>

    @Query("SELECT * FROM equipment WHERE id = :equipmentId")
    fun getEquipmentById(equipmentId: Int): Flow<EquipmentEntity>
}
