package com.prafullkumar.trainx.home.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HydrationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val time: Long,
    val quantity: Float
)