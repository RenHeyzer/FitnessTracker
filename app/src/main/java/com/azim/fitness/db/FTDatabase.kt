package com.azim.fitness.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.azim.fitness.db.dao.DailyResultDao
import com.azim.fitness.db.dao.ExercisesDao
import com.azim.fitness.db.dao.UserDao
import com.azim.fitness.db.entity.CalendarDay
import com.azim.fitness.db.entity.Exercise
import com.azim.fitness.db.entity.User
import com.azim.fitness.db.entity.Weight

@Database(
    entities = [User::class, Weight::class, CalendarDay::class, Exercise::class],
    version = 1,
)
abstract class FTDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val dailyResultDao: DailyResultDao
    abstract val exercisesDao: ExercisesDao

    companion object {
        const val DATABASE_NAME = "fitness_tracker_db"
    }
}