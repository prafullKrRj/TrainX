package com.prafullkumar.workout.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(
    tableName = "exercises"
)
data class ExerciseEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    @ColumnInfo(name = "equipmentUsed")
    val equipmentUsed: Int,  // Foreign key to equipment table
    val youtubeTutorial: String,
    @TypeConverters(StringListConverter::class)
    val muscles: List<String>,
    val instructions: String
)

class StringListConverter {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(",").map { it.trim() }
    }

    @TypeConverter
    fun toString(list: List<String>): String {
        return list.joinToString(",")
    }
}
