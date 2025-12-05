package com.azim.fitness.ui.history.tabs.weight

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.azim.fitness.data.repository.UserRepository
import com.azim.fitness.db.entity.Weight
import kotlinx.coroutines.launch

class WeightHistoryViewModel(
    private val date: String? = null,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _weights = MutableLiveData(emptyList<Weight>())
    val weights: LiveData<List<Weight>> = _weights

    init {
        getTodayWeight()
    }

    private fun getTodayWeight() {
        if (date == null) return
        viewModelScope.launch {
            userRepository.getTodayWeights(date).collect { weights ->
                _weights.value = weights
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class WeightHistoryViewModelFactory(
    private val date: String? = null,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeightHistoryViewModel::class.java)) {
            return WeightHistoryViewModel(
                date,
                userRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}