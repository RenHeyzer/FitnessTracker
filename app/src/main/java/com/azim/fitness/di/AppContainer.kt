package com.azim.fitness.di

import android.content.Context
import androidx.room.Room
import com.azim.fitness.data.repository.UserRepository
import com.azim.fitness.db.FTDatabase
import com.azim.fitness.preferences.PreferencesHelper

class AppContainer(context: Context) {
    private val ftDatabase: FTDatabase = Room.databaseBuilder(
        context = context.applicationContext,
        FTDatabase::class.java,
        FTDatabase.DATABASE_NAME
    ).build()

    val userDao = ftDatabase.profileDao
    val preferencesHelper = PreferencesHelper(context)

    val userRepository = UserRepository(userDao)
}