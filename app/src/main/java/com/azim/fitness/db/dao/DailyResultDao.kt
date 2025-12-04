package com.azim.fitness.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.azim.fitness.db.entity.CalendarDay
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyResultDao {

    @Query("SELECT * FROM calendar_day")
    fun getDailyResults(): Flow<List<CalendarDay>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDailyResult(day: CalendarDay)
}