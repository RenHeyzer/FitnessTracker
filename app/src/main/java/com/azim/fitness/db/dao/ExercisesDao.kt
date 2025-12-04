package com.azim.fitness.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.azim.fitness.db.entity.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExercisesDao {

    @Query("SELECT COUNT(id) FROM exercise LIMIT 1")
    suspend fun countRecords(): Int

    @Query("SELECT * FROM exercise")
    fun getLocalExercises(): Flow<List<Exercise>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExercises(exercises: List<Exercise>)

    @Query("UPDATE exercise SET completed = :completed WHERE id = :id")
    suspend fun updateExercise(id: Int, completed: Boolean)

    @Query("DELETE FROM exercise")
    suspend fun clearAll()
}