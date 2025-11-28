package com.azim.fitness.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithWeight(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "ownerId"
    )
    val weights: List<Weight>
)