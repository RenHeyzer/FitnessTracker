package com.azim.fitness

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.azim.fitness.data.repository.DailyResultRepository
import com.azim.fitness.data.repository.ExercisesRepository
import com.azim.fitness.data.repository.UserRepository
import com.azim.fitness.db.entity.CalendarDay
import com.azim.fitness.db.entity.DayStatus
import com.azim.fitness.preferences.PreferencesHelper
import com.azim.fitness.utils.toLocalDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate

class SaveDailyResultViewModel(
    private val preferencesHelper: PreferencesHelper,
    private val userRepository: UserRepository,
    private val exercisesRepository: ExercisesRepository,
    private val dailyResultRepository: DailyResultRepository,
) : ViewModel() {

    init {
        doWork()
    }

    fun startIfNeeded() = Unit

    private fun doWork() {
        if (preferencesHelper.isAuthorized && preferencesHelper.isGoalsDefined) {
            checkAndSaveDailyResult()
        }
    }

    private fun saveExercisesToLocal() {
        viewModelScope.launch {
            exercisesRepository.addExercises(exercisesRepository.getExercises())
        }
    }

    private fun checkAndSaveDailyResult() {
        val today = LocalDate.now()
        val lastDate = preferencesHelper.lastDate

        if (lastDate == null || lastDate >= today) {
            preferencesHelper.lastDate = today
            return
        }

        viewModelScope.launch {
            processLastDate(lastDate)
            preferencesHelper.lastDate = today
        }
    }

    private suspend fun processLastDate(lastDate: LocalDate) {
        val status = defineStatus(lastDate)

        dailyResultRepository.addDailyResult(
            CalendarDay(
                date = lastDate,
                status = status,
                isCurrentMonth = true
            )
        )

        exercisesRepository.clearAllExercises()
        saveExercisesToLocal()
    }

    private suspend fun defineStatus(lastDate: LocalDate): DayStatus {
        val weight = userRepository.getCurrentWeight().firstOrNull()
        val wasYesterdayWeighing = weight?.timestamp?.toLocalDate() == lastDate

        val exercises = exercisesRepository.getLocalExercises().first()
        val allExercisesCompleted = exercises.all { exercise -> exercise.completed }

        return if (allExercisesCompleted && wasYesterdayWeighing) {
            DayStatus.COMPLETED
        } else {
            DayStatus.MISSED
        }
    }
}

@Suppress("UNCHECKED_CAST")
class SaveDailyResultViewModelFactory(
    private val preferencesHelper: PreferencesHelper,
    private val userRepository: UserRepository,
    private val exercisesRepository: ExercisesRepository,
    private val dailyResultRepository: DailyResultRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SaveDailyResultViewModel::class.java)) {
            return SaveDailyResultViewModel(
                preferencesHelper,
                userRepository,
                exercisesRepository,
                dailyResultRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}