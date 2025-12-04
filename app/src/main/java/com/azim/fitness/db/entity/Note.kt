package com.azim.fitness.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.azim.fitness.db.converters.InstantConverter
import java.time.Instant

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val noteId: Long = 0L,
    val ownerId: String,
    val noteText: String,
    @field:TypeConverters(InstantConverter::class)
    val timestamp: Instant = Instant.now()
)
