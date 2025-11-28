package com.azim.fitness.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise")
data class Exercise(
    @PrimaryKey
    val id: Int,
    val name: String,
    val technique: String? = null,
    val techniqueVideo: String? = null,
    val techniqueImg: Int? = null,
    val sets: Int,
    val reps: String,
    var completed: Boolean = false,
)