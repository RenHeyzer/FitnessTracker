package com.azim.fitness.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.azim.fitness.db.entity.Food
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface FoodsDao {

    @Query("SELECT * FROM food WHERE date = :date")
    fun getTodayFoods(date: String): Flow<List<Food>>

    @Query("SELECT SUM(calories) FROM food")
    fun getTotalCalories(): Flow<Float?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFood(food: Food): Long
}