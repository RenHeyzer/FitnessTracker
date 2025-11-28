package com.azim.fitness.data.models

data class Exercise(
    val id: Int,
    val name: String,
    val technique: String? = null,
    val techniqueVideo: String? = null,
    val techniqueImg: Int? = null,
    val sets: Int,
    val reps: String
)