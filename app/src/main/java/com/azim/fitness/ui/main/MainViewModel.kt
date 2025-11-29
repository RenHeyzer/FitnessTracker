package com.azim.fitness.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.azim.fitness.data.repository.DailyResultRepository
import com.azim.fitness.data.repository.ExercisesRepository
import com.azim.fitness.data.repository.UserRepository
import com.azim.fitness.db.entity.CalendarDay
import com.azim.fitness.db.entity.DayStatus
import com.azim.fitness.db.entity.Exercise
import com.azim.fitness.db.entity.Weight
import com.azim.fitness.preferences.PreferencesHelper
import com.azim.fitness.ui.goals.Goal
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class MainViewModel(
    private val exercisesRepository: ExercisesRepository,
    private val userRepository: UserRepository,
    private val dailyResultRepository: DailyResultRepository,
    private val preferencesHelper: PreferencesHelper,
) : ViewModel() {

    private val _exercises = MutableLiveData(listOf<Exercise>())
    val exercises: LiveData<List<Exercise>> = _exercises

    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int> = _progress

    init {
        saveExercisesLocal()
        getLocalExercises()
        calculateGoalProgress()
    }

    private fun saveExercisesLocal() {
        viewModelScope.launch {
            exercisesRepository.addExercises(exercisesRepository.getExercises())
        }
    }

    private fun getLocalExercises() {
        viewModelScope.launch {
            exercisesRepository.getLocalExercises().collect {
                _exercises.value = it
            }
        }
    }

    private fun calculateGoalProgress() {
        viewModelScope.launch {
            val firstWeight = userRepository.getFirstWeight()
            Log.e("firstWeight", firstWeight.toString())
            userRepository.getCurrentWeight().collect {
                when (preferencesHelper.goal) {
                    Goal.LOOSE_WEIGHT -> {
                        if (firstWeight != null) {
                            val weightDifference = firstWeight.weight - it.weight
                            val progress = (weightDifference / preferencesHelper.targetWeight) * 100
                            if (progress >= 0) {
                                _progress.value = progress.toInt()
                            }
                        }
                    }

                    Goal.GAIN_MUSCLE -> {
                        if (firstWeight != null) {
                            val weightDifference = it.weight - firstWeight.weight
                            val progress = (weightDifference / preferencesHelper.targetWeight) * 100
                            if (progress >= 0) {
                                _progress.value = progress.toInt()
                            }
                        }
                    }

                    Goal.MAINTAIN_FORM -> {
                        if (firstWeight != null) {
                            if (it.weight > firstWeight.weight) {
                                val weightDifference = it.weight - firstWeight.weight
                                val progress = 100 + weightDifference
                                _progress.value = progress.toInt()
                            } else {
                                val weightDifference = firstWeight.weight - it.weight
                                val progress = 100 - weightDifference
                                _progress.value = progress.toInt()
                            }
                        }
                    }
                }

            }
        }
    }

    fun addWeight(currentWeight: Float) {
        viewModelScope.launch {
            userRepository.addWeight(
                Weight(
                    ownerId = userRepository.getUserId(),
                    weight = currentWeight
                )
            )
        }
    }

    fun updateExercise(id: Int, completed: Boolean) {
        viewModelScope.launch {
            exercisesRepository.updateExercise(id, completed)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val exercisesRepository: ExercisesRepository,
    private val userRepository: UserRepository,
    private val dailyResultRepository: DailyResultRepository,
    private val preferencesHelper: PreferencesHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                exercisesRepository,
                userRepository,
                dailyResultRepository,
                preferencesHelper
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}