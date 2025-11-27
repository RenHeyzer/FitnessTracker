package com.azim.fitness.preferences

import android.content.Context

class PreferencesHelper(context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    var isAuthorized: Boolean
        set(value) = sharedPreferences.edit().putBoolean(AUTHORIZED_KEY, value).apply()
        get() = sharedPreferences.getBoolean(AUTHORIZED_KEY, false)

    var isGoalsDefined: Boolean
        set(value) = sharedPreferences.edit().putBoolean(GOALS_DEFINED_KEY, value).apply()
        get() = sharedPreferences.getBoolean(GOALS_DEFINED_KEY, false)

    companion object {
        private const val PREFERENCES_NAME = "preferences"
        const val AUTHORIZED_KEY = "authorized"
        const val GOALS_DEFINED_KEY = "goals"
    }
}