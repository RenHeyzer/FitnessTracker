package com.azim.fitness.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.azim.fitness.data.repository.ExercisesRepository
import com.azim.fitness.data.repository.NotesRepository
import com.azim.fitness.data.repository.UserRepository
import com.azim.fitness.db.entity.Exercise
import com.azim.fitness.db.entity.Note
import com.azim.fitness.db.entity.Weight
import com.azim.fitness.preferences.PreferencesHelper
import com.azim.fitness.ui.goals.Goal
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val exercisesRepository: ExercisesRepository,
    private val userRepository: UserRepository,
    private val preferencesHelper: PreferencesHelper,
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val _exercises = MutableLiveData(listOf<Exercise>())
    val exercises: LiveData<List<Exercise>> = _exercises

    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int> = _progress

    private val _noteEvents = MutableSharedFlow<NoteEvent>()
    val noteEvents = _noteEvents.asSharedFlow()

    private val _noteState = MutableLiveData(NoteUiState())
    val noteState: LiveData<NoteUiState> = _noteState

    init {
        getLocalExercises()
        calculateGoalProgress()
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
            userRepository.getCurrentWeight().collect { currentWeight ->
                currentWeight?.let {
                    when (preferencesHelper.goal) {
                        Goal.LOOSE_WEIGHT -> {
                            if (firstWeight != null) {
                                val weightDifference = firstWeight.weight - it.weight
                                val progress =
                                    (weightDifference / preferencesHelper.targetWeight) * 100
                                if (progress >= 0) {
                                    _progress.value = progress.toInt()
                                }
                            }
                        }

                        Goal.GAIN_MUSCLE -> {
                            if (firstWeight != null) {
                                val weightDifference = it.weight - firstWeight.weight
                                val progress =
                                    (weightDifference / preferencesHelper.targetWeight) * 100
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

    fun addNote(noteText: String) {
        if (noteText.isBlank()) {
            Log.e("empty", noteText)
            _noteState.value = _noteState.value?.copy(error = "Заполните поле")
            return
        }
        viewModelScope.launch {
            runCatching {
                val ownerId = userRepository.getUserId()
                val note = Note(
                    ownerId = ownerId,
                    noteText = noteText
                )
                notesRepository.addNote(note)
            }.onSuccess {
                _noteState.value = _noteState.value?.copy(noteId = it, error = null)
                _noteEvents.emit(NoteEvent.NoteSaved)
            }.onFailure {
                _noteState.value = _noteState.value?.copy(noteId = 0L, error = it.message)
                _noteEvents.emit(NoteEvent.ShowToast("Не удалось сохранить заметку"))
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val exercisesRepository: ExercisesRepository,
    private val userRepository: UserRepository,
    private val preferencesHelper: PreferencesHelper,
    private val notesRepository: NotesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                exercisesRepository,
                userRepository,
                preferencesHelper,
                notesRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

data class NoteUiState(
    val noteId: Long = 0L,
    val error: String? = null,
)

sealed class NoteEvent {
    data class ShowToast(val message: String): NoteEvent()
    object NoteSaved : NoteEvent()
}