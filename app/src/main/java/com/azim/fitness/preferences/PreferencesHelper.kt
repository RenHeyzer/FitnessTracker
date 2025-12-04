package com.azim.fitness.preferences

import android.content.Context
import com.azim.fitness.ui.goals.Goal
import java.time.LocalDate

class PreferencesHelper(context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    var isAuthorized: Boolean
        set(value) = sharedPreferences.edit().putBoolean(AUTHORIZED_KEY, value).apply()
        get() = sharedPreferences.getBoolean(AUTHORIZED_KEY, false)

    var isGoalsDefined: Boolean
        set(value) = sharedPreferences.edit().putBoolean(GOALS_DEFINED_KEY, value).apply()
        get() = sharedPreferences.getBoolean(GOALS_DEFINED_KEY, false)

    var goal: Goal
        set(value) = sharedPreferences.edit().putString(GOAL_KEY, value.name).apply()
        get() {
            val value = sharedPreferences.getString(GOAL_KEY, Goal.LOOSE_WEIGHT.name)
            return Goal.valueOf(value.toString())
        }

    var targetWeight: Float
        set(value) = sharedPreferences.edit().putFloat(TARGET_WEIGHT_KEY, value).apply()
        get() = sharedPreferences.getFloat(TARGET_WEIGHT_KEY, 0.0f)

    var lastDate: LocalDate?
        set(value) = sharedPreferences.edit().putString(LAST_DATE_KEY, value.toString()).apply()
        get() = sharedPreferences.getString(LAST_DATE_KEY, null)?.let { LocalDate.parse(it) }

    companion object {
        private const val PREFERENCES_NAME = "preferences"
        const val AUTHORIZED_KEY = "authorized"
        const val GOALS_DEFINED_KEY = "goals"
        const val GOAL_KEY = "goal"
        const val TARGET_WEIGHT_KEY = "target_weight"
        const val LAST_DATE_KEY = ""
    }
}