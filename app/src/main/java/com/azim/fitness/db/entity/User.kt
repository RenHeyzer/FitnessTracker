package com.azim.fitness.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
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
