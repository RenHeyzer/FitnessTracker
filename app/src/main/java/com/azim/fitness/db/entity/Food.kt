package com.azim.fitness.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.azim.fitness.db.converters.InstantConverter
import java.time.Instant

@Entity(tableName = "food")
data class Food(
    @PrimaryKey(autoGenerate = true)
    val foodId: Int = 0,
    val ownerId: String,
    val foodName: String,
    val calories: Float,
    @field:TypeConverters(InstantConverter::class)
    val timestamp: Instant = Instant.now()
)