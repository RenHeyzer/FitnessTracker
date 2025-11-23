package com.azim.fitness

import android.app.Application
import android.content.Context
import com.azim.fitness.di.AppContainer

class FitnessTrackerApp : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(applicationContext)
    }
}

val Context.container: AppContainer
    get() = (applicationContext as FitnessTrackerApp).container