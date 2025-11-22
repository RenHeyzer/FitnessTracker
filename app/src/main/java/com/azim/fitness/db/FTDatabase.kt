package com.azim.fitness.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.azim.fitness.db.dao.ProfileDao
import com.azim.fitness.db.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    autoMigrations = [AutoMigration(
        from = 1,
        to = 2
    )]
)
abstract class FTDatabase : RoomDatabase() {
    abstract val profileDao: ProfileDao

    companion object {
        const val DATABASE_NAME = "fitness_tracker_db"
    }
}