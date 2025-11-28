package com.azim.fitness.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.azim.fitness.db.converters.InstantConverter
import java.time.Instant

@Entity(
    tableName = "weight",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["ownerId"],
            onDelete = CASCADE
        )
    ],
    indices = [Index(value = ["ownerId"])]
)
data class Weight(
    @PrimaryKey(autoGenerate = true)
    val weightId: Int = 0,
    val ownerId: String,
    val weight: Float,
    @field:TypeConverters(InstantConverter::class)
    val timestamp: Instant = Instant.now()
)
