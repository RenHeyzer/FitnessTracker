package com.azim.fitness.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val lastname: String,
    val firstname: String,
    val age: Int,
    val weight: Float,
    val height: Float,
    val email: String,
    val lifestyle: Lifestyle,
)

enum class Lifestyle(val value: String) {
    SEDENTERY("Малоподвижный"),
    ACTIIVE("Активный")
}
