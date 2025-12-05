package com.azim.fitness.ui.history.tabs.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.azim.fitness.data.repository.NotesRepository
import com.azim.fitness.db.entity.Note
import kotlinx.coroutines.launch

class NotesHistoryViewModel(
    private val date: String? = null,
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val _notes = MutableLiveData(emptyList<Note>())
    val notes: LiveData<List<Note>> = _notes

    init {
        getTodayNotes()
    }

    private fun getTodayNotes() {
        if (date == null) return
        viewModelScope.launch {
            notesRepository.getTodayNotes(date).collect { notes ->
                _notes.value = notes
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class NotesHistoryViewModelFactory(
    private val date: String? = null,
    private val notesRepository: NotesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesHistoryViewModel::class.java)) {
            return NotesHistoryViewModel(
                date,
                notesRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}