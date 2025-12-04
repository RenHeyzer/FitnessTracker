package com.azim.fitness.di

import android.content.Context
import androidx.room.Room
import com.azim.fitness.data.repository.DailyResultRepository
import com.azim.fitness.data.repository.ExercisesRepository
import com.azim.fitness.data.repository.NotesRepository
import com.azim.fitness.data.repository.UserRepository
import com.azim.fitness.db.FTDatabase
import com.azim.fitness.preferences.PreferencesHelper

class AppContainer(context: Context) {
    private val ftDatabase: FTDatabase = Room.databaseBuilder(
        context = context.applicationContext,
        FTDatabase::class.java,
        FTDatabase.DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    val userDao = ftDatabase.userDao
    val dailyResultDao = ftDatabase.dailyResultDao
    val exercisesDao = ftDatabase.exercisesDao
    val notesDao = ftDatabase.notesDao
    val preferencesHelper = PreferencesHelper(context)

    val userRepository = UserRepository(userDao)
    val exercisesRepository = ExercisesRepository(preferencesHelper, exercisesDao)
    val dailyResultRepository = DailyResultRepository(dailyResultDao)
    val notesRepository by lazy {  NotesRepository(notesDao) }
}