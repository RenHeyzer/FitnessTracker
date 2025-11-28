package com.azim.fitness.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.azim.fitness.db.entity.User
import com.azim.fitness.db.entity.Weight
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getUserInfo(): Flow<User>

    @Query("SELECT id FROM user LIMIT 1")
    suspend fun getUserId(): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWeight(weight: Weight)

    @Query("SELECT * FROM weight ORDER BY timestamp DESC LIMIT 1")
    fun getCurrentWeight(): Flow<Weight>

    @Query("SELECT * FROM weight WHERE weightId = 1")
    suspend fun getFirstWeight(): Weight?
}