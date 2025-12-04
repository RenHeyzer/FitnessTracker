package com.azim.fitness.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.azim.fitness.data.repository.ExercisesRepository
import kotlinx.coroutines.launch

class GoalsViewModel(private val exercisesRepository: ExercisesRepository) : ViewModel() {

    fun saveExercisesToLocal() {
        viewModelScope.launch {
            exercisesRepository.addExercises(exercisesRepository.getExercises())
        }
    }
}

@Suppress("UNCHECKED_CAST")
class GoalsViewModelFactory(
    private val exercisesRepository: ExercisesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalsViewModel::class.java)) {
            return GoalsViewModel(
                exercisesRepository,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}